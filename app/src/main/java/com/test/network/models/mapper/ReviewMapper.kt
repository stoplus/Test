package com.test.network.models.mapper

import com.test.network.models.api.response.UserResponse
import com.test.network.models.api.response.ReviewResponse
import com.test.network.models.domain.ReviewResult
import com.test.network.models.domain.UserResult

fun ReviewResponse?.toDomain(): ReviewResult = this?.let {
    ReviewResult(
        id = it.id ?: 0,
        productId = it.productId ?: 0,
        rate = it.rate ?: 0,
        comment = it.comment ?: "",
        createdBy = it.createdBy.toDomain(),
        createdDate = it.createdDate ?: ""
    )
} ?: ReviewResult.emptyModel()

fun UserResponse?.toDomain(): UserResult = this?.let {
    UserResult(
        id = it.id ?: 0,
        username = it.username ?: "",
        firstName = it.firstName ?: "",
        lastName = it.lastName ?: "",
        email = it.email ?: "",
        photo = ""
    )
} ?: UserResult.emptyModel()