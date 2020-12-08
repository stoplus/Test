package com.test.ui.products

import com.test.data.PreferencesManager
import com.test.network.ApiInterface
import com.test.network.models.ReviewModel
import com.test.network.models.data.request.PostReviewRequest
import com.test.network.models.data.response.PostReviewResponse
import com.test.network.models.domain.ProductResult
import com.test.network.models.mapper.toDomain
import com.test.utils.CONTENT_TYPE_VALUE
import com.test.utils.TOKEN_PREFIX
import io.reactivex.Single

interface ProductUseCase {
    fun getProducts(): Single<MutableList<ProductResult>>
    fun getReviews(productId: Int): Single<MutableList<ReviewModel>>
    fun postReview(post: PostReviewRequest, productId: Int): Single<PostReviewResponse>
}

class ProductUseCaseImpl(
    private val api: ApiInterface,
    private val prefManager: PreferencesManager
) : ProductUseCase {

    override fun getProducts(): Single<MutableList<ProductResult>> {
        return api.getProducts()
            .flatMap {
                val list = it.map { response -> response.toDomain() }.toMutableList()
                Single.just(list)
            }
    }

    override fun getReviews(productId: Int): Single<MutableList<ReviewModel>> {
        return api.getReviews(productId)
    }

    override fun postReview(post: PostReviewRequest, productId: Int): Single<PostReviewResponse> {
        return api.postReview(
            TOKEN_PREFIX + prefManager.getToken(),
            CONTENT_TYPE_VALUE,
            productId,
            post
        )
    }
}