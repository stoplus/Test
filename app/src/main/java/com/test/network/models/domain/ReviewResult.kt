package com.test.network.models.domain

data class ReviewResult(
    var id: Int,
    var productId: Int,
    var rate: Int,
    var comment: String,
    var createdBy: UserResult,
    var createdDate: String
) {
    companion object {
        fun emptyModel() =
            ReviewResult(
                id = 0,
                productId = 0,
                rate = 0,
                comment = "",
                createdBy = UserResult.emptyModel(),
                createdDate = ""
            )
    }
}