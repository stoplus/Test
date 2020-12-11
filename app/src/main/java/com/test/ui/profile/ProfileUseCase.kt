package com.test.ui.profile

import com.test.data.PreferencesManager
import com.test.network.models.domain.UserResult

interface ProfileUseCase {
    fun saveProfile(userResponse: UserResult)
    fun getProfile(): UserResult
}

class ProfileUseCaseImpl(
    private val prefManager: PreferencesManager
) : ProfileUseCase {

    override fun saveProfile(userResponse: UserResult) {
        prefManager.saveProfile(userResponse)
    }

    override fun getProfile() = prefManager.getProfile()
}