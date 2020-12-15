package com.test.data

import android.content.SharedPreferences
import com.test.network.models.domain.UserResult
import com.test.utils.*

class PreferencesManager(private val pref: SharedPreferences) {

    companion object {
        const val NAME_USER = "nameUser"
        const val SURNAME_USER = "surnameUser"
        const val PHOTO_USER = "photoUser"
    }

    fun saveToken(token: String) {
        pref.edit().putString(TOKEN, token).apply()
    }

    fun getToken() = pref.getString(TOKEN, "") ?: ""

    fun getProfile(): UserResult {
        val nameUser = pref.getString(NAME_USER, "") ?: ""
        val surnameUser = pref.getString(SURNAME_USER, "") ?: ""
        val photoUserLink = pref.getString(PHOTO_USER, "") ?: ""
        return UserResult(firstName = nameUser, lastName = surnameUser, photo = photoUserLink)
    }

    fun saveProfile(userResponse: UserResult) {
        pref.edit().also {
            it.putString(NAME_USER, userResponse.firstName)
            it.putString(SURNAME_USER, userResponse.lastName)
            it.putString(PHOTO_USER, userResponse.photo)
        }.apply()
    }
}