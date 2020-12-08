package com.test.ui.profile

import com.test.data.PreferencesManager
import com.test.network.models.UserModel

interface ProfileUseCase {
    fun saveProfile(user: UserModel)
    fun getProfile(): UserModel
}

class ProfileUseCaseImpl(
    private val prefManager: PreferencesManager
) : ProfileUseCase {

    override fun saveProfile(user: UserModel) {
        prefManager.saveProfile(user)
    }

    override fun getProfile(): UserModel = prefManager.getProfile()
}