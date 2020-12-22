package com.test.ui.login

import android.os.Bundle
import android.view.View
import com.test.R
import com.test.base.BaseFragment
import com.test.databinding.FragmentLoginBinding
import com.test.network.models.domain.LoginResult
import com.test.utils.setMessage
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast

class FragmentLogin : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener { login() }
        binding.registerLink.setOnClickListener {
            router?.navigate(R.id.action_fragmentLogin_to_passwordFragment)
        }
        binding.loginUserName.requestFocus()
    }

    private fun login() {
        if (validate()) {
            subscribe(viewModel.login(
                binding.loginUserName.text.toString().trim(),
                binding.loginPassword.text.toString().trim()
            ),
                { enter(it) },
                { longToast(setMessage(it, mContext)) }
            )
        }
    }

    private fun enter(response: LoginResult) {
        if (response.success) {
            router?.toMain()
        } else {
            if (response.message.isNotEmpty()) {
                toast(response.message)
            }
            binding.loginUserName.requestFocus()
        }
    }

    private fun validate(): Boolean {
        var valid = true
        if (binding.loginUserName.text.toString().isEmpty()) {
            binding.loginUserName.error = resources.getString(R.string.login_error_enter_login)
            valid = false
        }
        if (binding.loginPassword.text.toString().isEmpty()) {
            binding.loginPassword.error = resources.getString(R.string.login_error_enter_password)
            valid = false
        }
        return valid
    }
}