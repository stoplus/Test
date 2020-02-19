package com.test.ui.products

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import com.test.R
import com.test.base.BaseActivity
import com.test.ui.MainViewModel
import com.test.ui.login.ActivityLogin
import com.test.ui.profile.FragmentProfile
import kotlinx.android.synthetic.main.view_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityProduct : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val viewModel by viewModel<MainViewModel>()
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_for_activity)

        val toolbar: Toolbar = findViewById(R.id.catalog_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.also { it.setDisplayShowTitleEnabled(false) }

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
            // set/show icon
            navigationView.menu.getItem(0).isVisible = !it
            navigationView.menu.getItem(1).isVisible = it
            navigationView.menu.getItem(2).isVisible = it
        })

        //init icon
        val logged = viewModel.isLogged()
        navigationView.menu.getItem(0).isVisible = !logged
        navigationView.menu.getItem(1).isVisible = logged
        navigationView.menu.getItem(2).isVisible = logged

        showFragment(
            FragmentProductList.newInstance(),
            R.id.container_for_fragments,
            FragmentProductList.TAG
        )
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_login -> ActivityLogin.start(this)
            R.id.nav_logout -> viewModel.logout()
            R.id.nav_profile -> showFragment(
                FragmentProfile.newInstance(), R.id.container_for_fragments, FragmentProfile.TAG
            )
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        //check fragments
        val listFragments = supportFragmentManager.fragments.filter { frag -> frag.isVisible }
        when (listFragments[listFragments.size - 1]) {
            is FragmentProductList -> finish()
            is FragmentProfile -> {
                catalog_toolbar.visibility = View.VISIBLE
                super.onBackPressed()
            }
            else -> super.onBackPressed()
        }
    }
}