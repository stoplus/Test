package com.test.ui.products.productDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.base.BaseViewModel
import com.test.network.models.api.request.PostReviewRequest
import com.test.network.models.api.response.PostReviewResponse
import com.test.network.models.domain.ProductResult
import com.test.network.models.domain.ReviewResult
import com.test.ui.login.LoginUseCase
import com.test.ui.products.ProductUseCase
import io.reactivex.Single

abstract class ProductDetailViewModel : BaseViewModel() {

    abstract val productLiveData: LiveData<ProductResult>

    abstract fun getProducts(): Single<MutableList<ProductResult>>
    abstract fun getReviews(): Single<MutableList<ReviewResult>>
    abstract fun postReview(reviewRequest: PostReviewRequest): Single<PostReviewResponse>
    abstract fun isLogged(): Boolean
    abstract fun logout()
}

class ProductDetailViewModelImpl(
    private val product: ProductResult,
    private val loginUseCase: LoginUseCase,
    private val productUseCase: ProductUseCase
) : ProductDetailViewModel() {

    override val productLiveData = MutableLiveData<ProductResult>()

    init {
        productLiveData.value = product
    }

    override fun getProducts(): Single<MutableList<ProductResult>> {
        return productUseCase.getProducts()
    }

    override fun getReviews(): Single<MutableList<ReviewResult>> {
        return productUseCase.getReviews(product.id)
    }

    override fun postReview(reviewRequest: PostReviewRequest): Single<PostReviewResponse> {
        return productUseCase.postReview(reviewRequest, product.id)
    }

    override fun isLogged() = loginUseCase.isLogged()
    override fun logout() {
        loginUseCase.logout()
    }
}