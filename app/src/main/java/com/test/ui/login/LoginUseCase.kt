package com.test.ui.login

import com.test.data.AuthManager
import com.test.data.PreferencesManager
import com.test.network.models.domain.LoginResult
import com.test.network.ApiInterface
import com.test.network.models.domain.RegisterResult
import com.test.network.models.domain.UserResult
import com.test.network.models.mapper.toDomain
import io.reactivex.Single

interface LoginUseCase {
    fun login(login: String, pass: String): Single<LoginResult>
    fun register(login: String, pass: String): Single<RegisterResult>
    fun isLogged(): Boolean
    fun logout()
}

class LoginUseCaseImpl(
    private val api: ApiInterface,
    private val prefManager: PreferencesManager,
    private val authManager: AuthManager
) : LoginUseCase {

    override fun login(login: String, pass: String): Single<LoginResult> {
        return api.login(login, pass)
            .map {
                it.toDomain().apply {
                    if (success) {
                        prefManager.saveToken(token)
                        authManager.isLoggedSubject.onNext(true)
                    }
                }
            }
    }

    override fun register(login: String, pass: String): Single<RegisterResult> {
        return api.register(login, pass)
            .map {
                it.toDomain().apply {
                    if (success) {
                        prefManager.saveToken(token)
                        authManager.isLoggedSubject.onNext(true)
                    }
                }
            }
    }

    override fun isLogged(): Boolean {
        return prefManager.getToken().isNotEmpty()
    }

    override fun logout() {
        prefManager.saveToken("")
        prefManager.saveProfile(UserResult.emptyModel())
        authManager.isLoggedSubject.onNext(false)
    }
}