package com.test.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.R
import com.test.base.BaseFragment
import com.test.databinding.FragmentRegisterBinding
import com.test.network.models.domain.RegisterResult
import com.test.ui.MainViewModel
import com.test.utils.clickBtn
import com.test.utils.setMessage
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast

class FragmentRegister : BaseFragment<MainViewModel>() {

    private val scope by lazy { AndroidLifecycleScopeProvider.from(this) }
    private var bindingNull: FragmentRegisterBinding? = null
    private val binding get() = bindingNull!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingNull = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.registerBtn.clickBtn(scope) {
            if (validate()) {
                subscribe(
                    viewModel.register(
                        binding.registerLogin.text.toString().trim(),
                        binding.registerPassword.text.toString().trim()
                    ), { enter(it) }, { context?.also { con -> longToast(setMessage(it, con)) } })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerLogin.requestFocus()
    }

    private fun enter(response: RegisterResult) {
        if (response.success) {
            router?.navigate(R.id.action_fragmentRegister_to_fragmentProfile)
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

    override fun onDestroyView() {
        super.onDestroyView()
        bindingNull = null
    }
}