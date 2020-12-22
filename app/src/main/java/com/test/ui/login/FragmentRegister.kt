package com.test.ui.login

import android.os.Bundle
import android.view.View
import com.test.R
import com.test.base.BaseFragment
import com.test.databinding.FragmentRegisterBinding
import com.test.network.models.domain.RegisterResult
import com.test.utils.setMessage
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast

class FragmentRegister : BaseFragment<LoginViewModel, FragmentRegisterBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerBtn.setOnClickListener {
            if (validate()) {
                subscribe(viewModel.register(
                    binding.registerLogin.text.toString().trim(),
                    binding.registerPassword.text.toString().trim()
                ), { enter(it) }, { longToast(setMessage(it, mContext)) })
            }
        }
    }

    private fun enter(response: RegisterResult) {
        if (response.success) {
            router?.toMain()
        } else {
            if (response.message.isNotEmpty()) {
                toast(response.message)
            }
        }
    }

    private fun validate(): Boolean {
        var valid = true
        if (binding.registerLogin.text.toString().isEmpty()) {
            binding.registerLogin.error = resources.getString(R.string.login_error_enter_login)
            valid = false
        }
        if (binding.registerPassword.text.toString().isEmpty()) {
            binding.registerPassword.error = resources.getString(R.string.login_error_enter_password)
            valid = false
        }
        if (binding.registerPasswordRepeat.text.toString().isEmpty()) {
            binding.registerPasswordRepeat.error = resources.getString(R.string.login_password_repeat_hint)
            valid = false
        }
        if (binding.registerPassword.text.toString() != binding.registerPasswordRepeat.text.toString()) {
            binding.registerPasswordRepeat.error = resources.getString(R.string.login_error_password_not_match)
            binding.registerPassword.error = resources.getString(R.string.login_error_password_not_match)
            valid = false
        }
        return valid
    }
}