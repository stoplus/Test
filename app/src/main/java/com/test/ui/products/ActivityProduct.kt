package com.test.ui.products

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.test.R
import com.test.base.BaseActivity
import com.test.databinding.ActivityMainBinding
import com.test.router.Router
import com.test.ui.MainViewModel
import com.test.ui.login.ActivityLogin

class ActivityProduct : BaseActivity<MainViewModel>(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.catalog_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.also { it.setDisplayShowTitleEnabled(false) }

        drawer = binding.drawerLayout
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

        viewModel.isLoggedLiveData.observe(this, {
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

        router = Router(this, R.id.container_for_fragments)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_login -> ActivityLogin.start(this)
            R.id.nav_logout -> viewModel.logout()
            R.id.nav_profile -> {
                binding.includeToolbar.root.visibility = View.GONE
                when (router?.currentDestination()?.label) {
                    "fragmentDetailProduct" -> router?.navigate(R.id.action_fragmentDetailProduct_to_fragmentProfile)
                    else -> router?.navigate(R.id.action_fragmentProductList_to_fragmentProfile)
                }
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.includeToolbar.root.visibility = View.VISIBLE
    }
}