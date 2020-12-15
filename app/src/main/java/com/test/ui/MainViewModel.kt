package com.test.ui

import androidx.lifecycle.LiveData
import com.test.base.BaseViewModel
import com.test.network.models.domain.LoginResult
import com.test.network.models.domain.RegisterResult
import com.test.ui.login.LoginUseCase
import com.test.ui.products.ProductUseCase
import com.test.ui.products.productList.FragmentProductListDirections
import io.reactivex.Single

abstract class MainViewModel : BaseViewModel() {

    abstract val isLoggedLiveData: LiveData<Boolean>

    abstract fun login(login: String, pass: String): Single<LoginResult>
    abstract fun register(login: String, pass: String): Single<RegisterResult>
    abstract fun isLogged(): Boolean
    abstract fun logout()
    abstract fun openProfile(id: Int)
}

class MainViewModelImpl(
    private val loginUseCase: LoginUseCase
) : MainViewModel() {

    override val isLoggedLiveData = loginUseCase.isLoggedLiveData
//    override val isLoggedLiveData = MutableLiveData<Boolean>()

    override fun register(login: String, pass: String): Single<RegisterResult> {
        return loginUseCase.register(login, pass)
//            .map { it.apply { if (success) isLoggedLiveData.postValue(true) } }
    }

    override fun login(login: String, pass: String): Single<LoginResult> {
        return loginUseCase.login(login, pass)
//            .map { it.apply { if (success) isLoggedLiveData.postValue(true) } }
    }

    override fun isLogged() = loginUseCase.isLogged()
    override fun logout() {
        loginUseCase.logout()
//        isLoggedLiveData.value = false
    }

    override fun openProfile(id: Int) {
        router?.navigate(FragmentProductListDirections.actionFragmentProductListToFragmentProfile())
    }
}