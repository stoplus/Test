package com.test.network.models.api.response

import com.google.gson.annotations.SerializedName

data class PostReviewResponse (

    @SerializedName("success")
    var success: Boolean
)