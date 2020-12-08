package com.test.router

import android.content.Intent
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.test.ui.login.ActivityLogin
import com.test.ui.products.ActivityProduct
import kotlin.reflect.KClass

class Router(
    private val activity: AppCompatActivity,
    @IdRes viewId: Int
) {

    private val navController: NavController = activity.findNavController(viewId)

    fun popAllExcludeFirst() {
        if (navController.currentDestination?.id != navController.graph.startDestination) {
            navController.popBackStack(navController.graph.startDestination, false)
        }
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun navigate(@IdRes resId: Int) {
        if (navController.currentDestination?.id != resId) {
            navController.navigate(resId)
        }
    }

    fun currentDestination(): NavDestination? {
       return navController.currentDestination
    }

    fun navigate(navDirections: NavDirections) {
        navController.navigate(navDirections)
    }

    fun toMain() {
        activity.finish()
        startClearActivity(ActivityProduct::class)
    }

    fun toLogin() {
        startClearActivity(ActivityLogin::class)
    }

    private fun startClearActivity(clazz: KClass<*>) {
        activity.startActivity(
            Intent(activity, clazz.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }
}