package com.test.network.models.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class LoginResult(
    var success: Boolean,
    var token: String,
    var message: String
) : Parcelable {

    companion object {
        fun emptyModel() =
            LoginResult(
                success = false,
                token = "",
                message = ""
            )
    }
}