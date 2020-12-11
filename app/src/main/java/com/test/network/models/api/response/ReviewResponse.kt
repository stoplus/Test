package com.test.network.models.api.response

import com.google.gson.annotations.SerializedName

class ReviewResponse (

    @SerializedName("id")
    var id: Int?,

    @SerializedName("product")
    var productId: Int?,

    @SerializedName("rate")
    var rate: Int?,

    @SerializedName("text")
    var comment: String?,

    @SerializedName("created_by")
    var createdBy: UserResponse?,

    @SerializedName("created_at")
    var createdDate: String?
)