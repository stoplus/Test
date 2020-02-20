package com.test.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.R
import com.test.base.BaseFragment
import com.test.network.models.response.LoginResponse
import com.test.ui.MainViewModel
import com.test.utils.clickBtn
import com.test.utils.setMessage
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import kotlinx.android.synthetic.main.container_for_activity.*
import kotlinx.android.synthetic.main.fragment_register.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentRegister : BaseFragment() {

    private val scope by lazy { AndroidLifecycleScopeProvider.from(this) }
    private val viewModel by sharedViewModel<MainViewModel>()

    companion object {
        const val TAG = "FragmentRegister"
        fun newInstance() = FragmentRegister()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onResume() {
        super.onResume()

        registerBtn.clickBtn(scope) {
            if (validate()) {
                subscribe(
                    viewModel.register(
                        registerLogin.text.toString().trim(),
                        registerPassword.text.toString().trim()
                    ), { enter(it) }, { context?.also { con -> longToast(setMessage(it, con)) } })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.also {
            it.include_toolbar.visibility = View.GONE
            registerLogin.requestFocus()
        }
    }

    private fun enter(response: LoginResponse) {
        if (response.success) {
            activity?.also { it.finish() }
        } else {
            if (response.message.isNotEmpty()) {
                toast(response.message)
            }
        }
    }

    private fun validate(): Boolean {
        var valid = true
        if (registerLogin.text.toString().isEmpty()) {
            registerLogin.error = resources.getString(R.string.login_error_enter_login)
            valid = false
        }
        if (registerPassword.text.toString().isEmpty()) {
            registerPassword.error = resources.getString(R.string.login_error_enter_password)
            valid = false
        }
        if (registerPasswordRepeat.text.toString().isEmpty()) {
            registerPasswordRepeat.error = resources.getString(R.string.login_password_repeat_hint)
            valid = false
        }
        if (registerPassword.text.toString() != registerPasswordRepeat.text.toString()) {
            registerPasswordRepeat.error = resources.getString(R.string.login_error_password_not_match)
            registerPassword.error = resources.getString(R.string.login_error_password_not_match)
            valid = false
        }
        return valid
    }
}