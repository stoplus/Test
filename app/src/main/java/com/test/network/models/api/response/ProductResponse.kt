package com.test.network.models.api.response

import com.google.gson.annotations.SerializedName

data class ProductResponse (
    @SerializedName("id")
    var id: Int?,

    @SerializedName("title")
    var title: String?,

    @SerializedName("img")
    var img: String?,

    @SerializedName("text")
    var description: String?
)