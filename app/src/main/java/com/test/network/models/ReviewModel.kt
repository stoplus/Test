package com.test.network.models

import com.google.gson.annotations.SerializedName

class ReviewModel (

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("product")
    var productId: Int = 0,

    @SerializedName("rate")
    var rate: Int = 0,

    @SerializedName("text")
    var comment: String = "",


    @SerializedName("created_by")
    var createdBy: UserModel? = null,

    @SerializedName("created_at")
    var createdDate: String = ""
)