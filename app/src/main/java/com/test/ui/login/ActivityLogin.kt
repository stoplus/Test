package com.test.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.test.R
import com.test.base.BaseActivity
import kotlinx.android.synthetic.main.view_toolbar.*

class ActivityLogin : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, ActivityLogin::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_for_activity)

        catalog_toolbar.visibility = View.GONE

        showFragment(FragmentLogin.newInstance(), R.id.container_for_fragments, FragmentLogin.TAG)
    }

    override fun onBackPressed() {
        val listFragments = supportFragmentManager.fragments.filter { frag -> frag.isVisible }
        val fragment = listFragments[listFragments.size - 1]
        if (fragment is FragmentLogin) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}