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
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FragmentLogin : BaseFragment() {

    private val scope by lazy { AndroidLifecycleScopeProvider.from(this) }
    private val viewModel by sharedViewModel<MainViewModel>()

    companion object {
        const val TAG = "FragmentLogin"
        fun newInstance() = FragmentLogin()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onResume() {
        super.onResume()

        login_btn.clickBtn(scope) { login() }
        registerLink.clickBtn(scope) {
            addFragment(
                FragmentRegister.newInstance(),
                R.id.container_for_fragments,
                FragmentRegister.TAG
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.also {
            it.include_toolbar.visibility = View.GONE
            login_user_name.requestFocus()
        }
    }

    private fun login() {
        if (validate()) {
            subscribe(
                viewModel.login(
                    login_user_name.text.toString().trim(),
                    login_password.text.toString().trim()
                ), { enter(it) }, { context?.also { con -> longToast(setMessage(it, con)) } })
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
            login_user_name.error = resources.getString(R.string.login_error_enter_login)
            valid = false
        }
        if (login_password.text.toString().isEmpty()) {
            login_password.error = resources.getString(R.string.login_error_enter_password)
            valid = false
        }
        return valid
    }
}