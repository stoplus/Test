package com.test.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.test.R
import com.test.base.BaseActivity
import com.test.base.EmptyViewModel
import com.test.databinding.ActivityLoginBinding
import com.test.router.Router

class ActivityLogin : BaseActivity<EmptyViewModel>() {

    lateinit var binding: ActivityLoginBinding

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, ActivityLogin::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        router = Router(this, R.id.loginNavFragment)
//        showFragment(FragmentLogin.newInstance(), R.id.container_for_fragments, FragmentLogin.TAG)
    }

//    override fun onBackPressed() {
//        val listFragments = supportFragmentManager.fragments.filter { frag -> frag.isVisible }
//        val fragment = listFragments[listFragments.size - 1]
//        if (fragment is FragmentLogin) {
//            finish()
//        }else if (fragment is FragmentProfile){
//            toast(resources.getString(R.string.profile_error_saved))
//        } else {
//            super.onBackPressed()
//        }
//    }
}