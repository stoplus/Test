package com.test.ui

import androidx.lifecycle.LiveData
import com.test.base.BaseViewModel
import com.test.network.models.domain.LoginResult
import com.test.network.models.domain.ProductResult
import com.test.network.models.domain.RegisterResult
import com.test.network.models.domain.UserResult
import com.test.ui.login.LoginUseCase
import com.test.ui.products.FragmentProductListDirections
import com.test.ui.products.ProductUseCase
import com.test.ui.profile.ProfileUseCase
import io.reactivex.Single

abstract class MainViewModel : BaseViewModel() {

    abstract val isLoggedLiveData: LiveData<Boolean>

    abstract fun login(login: String, pass: String): Single<LoginResult>
    abstract fun register(login: String, pass: String): Single<RegisterResult>
    abstract fun saveProfile(userResponse: UserResult)
    abstract fun getProfile(): UserResult
    abstract fun getProducts(): Single<MutableList<ProductResult>>
    abstract fun isLogged(): Boolean
    abstract fun logout()
    abstract fun openProfile(id: Int)
}

class MainViewModelImpl(
    private val loginUseCase: LoginUseCase,
    private val profileUseCase: ProfileUseCase,
    private val productUseCase: ProductUseCase
) : MainViewModel() {

    override val isLoggedLiveData = loginUseCase.isLoggedLiveData

    override fun register(login: String, pass: String): Single<RegisterResult> {
        return loginUseCase.register(login, pass)
    }

    override fun login(login: String, pass: String): Single<LoginResult> {
        return loginUseCase.login(login, pass)
    }

    override fun getProducts(): Single<MutableList<ProductResult>> {
        return productUseCase.getProducts()
    }

    override fun saveProfile(userResponse: UserResult) {
        profileUseCase.saveProfile(userResponse)
    }

    override fun getProfile() = profileUseCase.getProfile()

    override fun isLogged() = loginUseCase.isLogged()
    override fun logout() {
        loginUseCase.logout()
    }

    override fun openProfile(id: Int) {
        router?.navigate(FragmentProductListDirections.actionFragmentProductListToFragmentProfile())
    }
}