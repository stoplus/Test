package com.test.network.models.api.response

import com.google.gson.annotations.SerializedName

class UserResponse(

    @SerializedName("id")
    var id: Int?,

    @SerializedName("username")
    var username: String?,

    @SerializedName("first_name")
    var firstName: String?,

    @SerializedName("last_name")
    var lastName: String?,

    @SerializedName("email")
    var email: String?
)