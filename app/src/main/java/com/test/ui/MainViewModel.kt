package com.test.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.base.BaseViewModel
import com.test.data.AuthManager
import com.test.ui.login.LoginUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class MainViewModel : BaseViewModel() {
    abstract val isLoggedLiveData: LiveData<Boolean>

    abstract fun subscribeIsLogin()
    abstract fun isLogged(): Boolean
    abstract fun logout()
}

class MainViewModelImpl(
    private val loginUseCase: LoginUseCase,
    private val authManager: AuthManager
) : MainViewModel() {

    override var isLoggedLiveData = MutableLiveData<Boolean>()

    override fun subscribeIsLogin() {
        authManager.isLoggedSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isLoggedLiveData.value = it }
            .untilCleared()
    }

    override fun isLogged() = loginUseCase.isLogged()
    override fun logout() {
        loginUseCase.logout()
    }
}