package com.test.network.models.data.response

import com.google.gson.annotations.SerializedName

class LoginResponse (

    @SerializedName("success")
    var success: Boolean? = null,

    @SerializedName("token")
    var token: String? = null,

    @SerializedName("message")
    var message: String? = null
)