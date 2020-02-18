package com.test.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.R
import com.test.base.BaseFragment
import com.test.network.models.response.LoginResponse
import com.test.ui.MainViewModel
import com.test.ui.products.ProductFragment
import com.test.utils.clickBtn
import com.test.utils.setMessage
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import kotlinx.android.synthetic.main.container_for_activity.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : BaseFragment() {

    private val scope by lazy { AndroidLifecycleScopeProvider.from(this) }
    private val viewModel by sharedViewModel<MainViewModel>()

    companion object {
        const val TAG = "LoginFragment"

        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.also {
            it.main_toolbar.visibility = View.GONE
            login_user_name.requestFocus()
        }

        login_btn.clickBtn(scope) { login() }
        registerLink.clickBtn(scope) {
            addFragment(
                RegisterFragment.newInstance(),
                R.id.container_for_fragments,
                RegisterFragment.TAG
            )
        }
        login_user_name.setText("ss")
        login_password.setText("ss")
    }

    private fun login() {
        if (validate()) {
            subscribe(
                viewModel.login(
                    login_user_name.text.toString().trim(),
                    login_password.text.toString().trim()
                ), {
                    enter(it)
                }, {
                    context?.also { con -> longToast(setMessage(it, con)) }
                })
        }
    }

    private fun enter(response: LoginResponse) {
        if (response.success) {
            activity?.also { it.finish() }
        } else {
            if (response.message.isNotEmpty()) {
                toast(response.message)
            }
            login_user_name.requestFocus()
        }
    }

    private fun validate(): Boolean {
        var valid = true
        if (login_user_name.text.toString().isEmpty()) {
            login_user_name.error = "Введите логин"
            valid = false
        }
        if (login_password.text.toString().isEmpty()) {
            login_password.error = "Введите пароль"
            valid = false
        }
        return valid
    }
}