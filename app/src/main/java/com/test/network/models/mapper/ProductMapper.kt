package com.test.network.models.mapper

import com.test.network.models.domain.ProductResult
import com.test.network.models.api.response.ProductResponse



fun ProductResponse?.toDomain(): ProductResult = this?.let {
    ProductResult(
        id = it.id ?: 0,
        title = it.title ?: "",
        img = it.img ?: "",
        description = it.description ?: ""
    )
} ?: ProductResult.emptyModel()