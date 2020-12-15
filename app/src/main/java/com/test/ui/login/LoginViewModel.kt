package com.test.ui.login

import com.test.base.BaseViewModel
import com.test.network.models.domain.LoginResult
import com.test.network.models.domain.RegisterResult
import io.reactivex.Single

abstract class LoginViewModel: BaseViewModel() {
    abstract fun login(login: String, pass: String): Single<LoginResult>
    abstract fun register(login: String, pass: String): Single<RegisterResult>
    abstract fun isLogged(): Boolean
    abstract fun logout()
}
class LoginViewModelImpl(
    private val loginUseCase: LoginUseCase
) : LoginViewModel() {

    override fun register(login: String, pass: String): Single<RegisterResult> {
        return loginUseCase.register(login, pass)
    }

    override fun login(login: String, pass: String): Single<LoginResult> {
        return loginUseCase.login(login, pass)
    }

    override fun isLogged() = loginUseCase.isLogged()
    override fun logout() {
        loginUseCase.logout()
    }
}