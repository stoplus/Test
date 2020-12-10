package com.test.ui.login

import android.os.Bundle
import com.test.R
import com.test.base.BaseActivity
import com.test.base.EmptyViewModel
import com.test.databinding.ActivityLoginBinding
import com.test.router.Router

class ActivityLogin : BaseActivity<EmptyViewModel>() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        router = Router(this, R.id.loginNavFragment)
    }
}