package com.test.ui.login

import android.os.Bundle
import com.test.R
import com.test.base.BaseActivity
import com.test.base.EmptyViewModel
import com.test.databinding.ActivityLoginBinding
import com.test.router.Router

class ActivityLogin : BaseActivity<EmptyViewModel, ActivityLoginBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        router = Router(this, R.id.login_nav_fragment)
    }
}