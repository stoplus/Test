package com.test.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.R
import com.test.base.BaseFragment
import com.test.network.models.domain.LoginResult
import com.test.databinding.FragmentLoginBinding
import com.test.ui.MainViewModel
import com.test.utils.clickBtn
import com.test.utils.setMessage
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast

class FragmentLogin : BaseFragment<MainViewModel>() {

    private val scope by lazy { AndroidLifecycleScopeProvider.from(this) }
    private var bindingNull: FragmentLoginBinding? = null
    private val binding get() = bindingNull!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingNull = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.loginBtn.clickBtn(scope) { login() }
        binding.registerLink.clickBtn(scope) {
            router?.navigate(R.id.action_fragmentLogin_to_passwordFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginUserName.requestFocus()
    }

    private fun login() {
        if (validate()) {
            subscribe(
                viewModel.login(
                    binding.loginUserName.text.toString().trim(),
                    binding.loginPassword.text.toString().trim()
                ),
                { enter(it) },
                { context?.also { con -> longToast(setMessage(it, con)) } }
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

    override fun onDestroyView() {
        super.onDestroyView()
        bindingNull = null
    }
}