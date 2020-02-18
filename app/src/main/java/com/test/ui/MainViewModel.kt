package com.test.ui

import androidx.lifecycle.ViewModel
import com.test.data.ApiManager
import com.test.network.models.ProductModel
import com.test.network.models.ReviewModel
import com.test.network.models.UserModel
import com.test.network.models.request.PostReviewRequest
import com.test.network.models.response.LoginResponse
import com.test.network.models.response.PostReviewResponse
import io.reactivex.Single

class MainViewModel(
    private val apiManager: ApiManager
) : ViewModel() {

    var productsList = apiManager.productsList
    var isLoggedLiveData = apiManager.isLoggedLiveData

    fun register(login: String, pass: String): Single<LoginResponse> {
        return apiManager.register(login, pass)
    }

    fun login(login: String, pass: String): Single<LoginResponse> {
        return apiManager.login(login, pass)
    }

    fun getProducts(): Single<MutableList<ProductModel>> {
        return apiManager.getProducts()
    }

    fun getReviews(productId: Int): Single<MutableList<ReviewModel>> {
        return apiManager.getReviews(productId)
    }

    fun isLogged(): Boolean {
        return apiManager.isLogged()
    }

    fun logout(){
        apiManager.logout()
    }

    fun postReview(post: PostReviewRequest, productId: Int): Single<PostReviewResponse> {
        return apiManager.postReview(post, productId)
    }

    fun saveProfile(user: UserModel){
        apiManager.saveProfile(user)
    }

    fun getProfile()= apiManager.getProfile()

}