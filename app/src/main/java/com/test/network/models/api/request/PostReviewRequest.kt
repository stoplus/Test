package com.test.network.models.api.request

import com.google.gson.annotations.SerializedName

data class PostReviewRequest (

    @SerializedName("rate")
    var rate: Int = 0,

    @SerializedName("text")
    var text: String = ""
)