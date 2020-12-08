package com.test.ui.login

import androidx.lifecycle.MutableLiveData
import com.test.data.PreferencesManager
import com.test.network.models.domain.LoginResult
import com.test.network.ApiInterface
import com.test.network.models.UserModel
import com.test.network.models.domain.RegisterResult
import com.test.network.models.mapper.toDomain
import io.reactivex.Single

interface LoginUseCase {

    var isLoggedLiveData: MutableLiveData<Boolean>

    fun login(login: String, pass: String): Single<LoginResult>
    fun register(login: String, pass: String): Single<RegisterResult>
    fun isLogged(): Boolean
    fun logout()
}

class LoginUseCaseImpl(
    private val api: ApiInterface,
    private val prefManager: PreferencesManager
) : LoginUseCase {

    override var isLoggedLiveData = MutableLiveData<Boolean>()

    override fun login(login: String, pass: String): Single<LoginResult> {
        return api.login(login, pass)
            .map {
                it.toDomain().apply {
                    if (success) {
                        prefManager.saveToken(token)
                        isLoggedLiveData.postValue(true)
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
                        isLoggedLiveData.postValue(true)
                    }
                }
            }
    }

    override fun isLogged(): Boolean {
        return prefManager.getToken().isNotEmpty()
    }

    override fun logout() {
        prefManager.saveToken("")
        prefManager.saveProfile(UserModel())
        isLoggedLiveData.postValue(false)
    }
}