package com.test.ui

import android.os.Bundle
import android.util.Log
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

class MainActivity : BaseActivity<MainViewModel>(), NavigationView.OnNavigationItemSelectedListener {

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

        viewModel.subscribeIsLogin()
        viewModel.isLoggedLiveData.observe(this, {
            showItemMenu(navigationView, it)
        })

        //init icon
        showItemMenu(navigationView, viewModel.isLogged())

        router = Router(this, R.id.container_for_fragments)
    }

    private fun showItemMenu(navigationView: NavigationView, shown: Boolean){
        navigationView.menu.findItem(R.id.nav_login).isVisible = !shown
        navigationView.menu.findItem(R.id.nav_profile).isVisible = shown
        navigationView.menu.findItem(R.id.nav_logout).isVisible = shown
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_login -> router?.toLogin()
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