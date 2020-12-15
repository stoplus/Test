package com.test.ui.profile

import com.test.base.BaseViewModel
import com.test.network.models.domain.UserResult

abstract class ProfileViewModel : BaseViewModel() {
    abstract fun saveProfile(userResponse: UserResult)
    abstract fun getProfile(): UserResult
}

class ProfileViewModelImpl(
    private val profileUseCase: ProfileUseCase,
) : ProfileViewModel() {

    override fun saveProfile(userResponse: UserResult) {
        profileUseCase.saveProfile(userResponse)
    }

    override fun getProfile() = profileUseCase.getProfile()
}