package com.test.network.models

import com.google.gson.annotations.SerializedName

class UserModel(

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("username")
    var username: String = "",

    @SerializedName("first_name")
    var firstName: String = "",

    @SerializedName("last_name")
    var lastName: String = "",

    @SerializedName("email")
    var email: String = ""
)