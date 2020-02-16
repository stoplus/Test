package com.test.data

import androidx.lifecycle.MutableLiveData
import com.test.network.ApiInterface
import com.test.network.models.ProductModel
import com.test.network.models.ReviewModel
import com.test.network.models.request.PostReviewRequest
import com.test.network.models.response.LoginResponse
import com.test.network.models.response.PostReviewResponse
import com.test.repository.ModelRepository
import com.test.utils.CONTENT_TYPE_VALUE
import com.test.utils.TOKEN_PREFIX
import io.reactivex.Single

class ApiManager(
    private val prefManager: PreferencesManager,
    private val modelRepository: ModelRepository,
    private val api: ApiInterface
) {

    var isLoggedLiveData = MutableLiveData<Boolean>().apply { value = false }
    var productsList = MutableLiveData<MutableList<ProductModel>>()

    fun register(login: String, pass: String): Single<LoginResponse> {
        return api.register(login, pass)
            .flatMap {
                if (it.success) {
                    prefManager.saveToken(it.token)
                    isLoggedLiveData.postValue(true)
                }
                Single.just(it)
            }
    }

    fun login(login: String, pass: String): Single<LoginResponse> {
        return api.login(login, pass)
            .flatMap {
                if (it.success) {
                    prefManager.saveToken(it.token)
                    isLoggedLiveData.postValue(true)
                }
                Single.just(it)
            }
    }

    fun getProducts(): Single<MutableList<ProductModel>> {
        return api.getProducts()
            .flatMap {
                productsList.postValue(it)
                Single.just(it)
            }
    }

    fun getReviews(productId: Int): Single<MutableList<ReviewModel>> {
        return api.getReviews(productId)
    }

    fun isLogged(): Boolean {
        return prefManager.getToken().isNotEmpty()
    }

    fun logout() {
        prefManager.saveToken("")
        isLoggedLiveData.postValue(false)
    }

    fun postReview(post: PostReviewRequest, productId: Int): Single<PostReviewResponse> {
        return api.postReview(
            TOKEN_PREFIX + prefManager.getToken(),
            CONTENT_TYPE_VALUE,
            productId,
            post
        )
    }
}
