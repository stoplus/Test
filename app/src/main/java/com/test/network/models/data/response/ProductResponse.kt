package com.test.network.models.data.response

import com.google.gson.annotations.SerializedName

data class ProductResponse (
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("img")
    var img: String? = null,

    @SerializedName("text")
    var description: String? = null
)