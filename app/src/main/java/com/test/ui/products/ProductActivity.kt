package com.test.ui.products

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.test.R
import com.test.base.BaseActivity
import com.test.ui.MainViewModel
import com.test.ui.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val viewModel by viewModel<MainViewModel>()
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_for_activity)

        val textToolbar: TextView = findViewById(R.id.text_toolbar)
        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_layout)
        val toggle =
            ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        viewModel.isLoggedLiveData.observe(this, Observer {
            navigationView.menu.getItem(0).isVisible = !it
            navigationView.menu.getItem(2).isVisible = it
        })

        showFragment(
            ProductFragment.newInstance(),
            R.id.container_for_fragments,
            ProductFragment.TAG
        )
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val id = menuItem.itemId
        if (id == R.id.nav_login) {
            LoginActivity.start(this)
        } else if (id == R.id.nav_sing_up) {

        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        val listFragments = supportFragmentManager.fragments.filter { frag -> frag.isVisible }
        val fragment = listFragments[listFragments.size - 1]
        if (fragment is ProductFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.logout()
    }
}