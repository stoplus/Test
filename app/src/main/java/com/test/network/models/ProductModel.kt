package com.test.network.models

import com.google.gson.annotations.SerializedName

data class ProductModel (
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("title")
    var title: String = "",

    @SerializedName("img")
    var img: String = "",

    @SerializedName("text")
    var description: String = ""

)