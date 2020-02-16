package com.test.network.models.response

import com.google.gson.annotations.SerializedName

class LoginResponse (

    @SerializedName("success")
    var success: Boolean = false,

    @SerializedName("token")
    var token: String = "",

    @SerializedName("message")
    var message: String = ""
)