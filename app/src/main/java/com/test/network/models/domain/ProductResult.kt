package com.test.network.models.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ProductResult(
    var id: Int,
    var title: String,
    var img: String,
    var description: String
) : Parcelable {

    companion object {
        fun emptyModel() =
            ProductResult(
                id = 0,
                title = "",
                img = "",
                description = ""
            )
    }
}