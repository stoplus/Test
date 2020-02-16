package com.test.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.test.R
import com.test.base.BaseActivity
import com.test.ui.MainViewModel
import kotlinx.android.synthetic.main.container_for_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {

    private val viewModel by viewModel<MainViewModel>()

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_for_activity)

        main_toolbar.visibility = View.GONE

        showFragment(LoginFragment.newInstance(), R.id.container_for_fragments, LoginFragment.TAG)
    }

    override fun onBackPressed() {
        val listFragments = supportFragmentManager.fragments.filter { frag -> frag.isVisible }
        val fragment = listFragments[listFragments.size - 1]
        if (fragment is LoginFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}