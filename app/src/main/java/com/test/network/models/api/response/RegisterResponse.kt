package com.test.network.models.api.response

import com.google.gson.annotations.SerializedName

class RegisterResponse(
    @SerializedName("success")
    var success: Boolean?,

    @SerializedName("token")
    var token: String?,

    @SerializedName("message")
    var message: String?
)