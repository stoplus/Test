package com.test.data

import com.test.network.ApiInterface
import com.test.repository.ModelRepository

class ApiManager(
    private val prefManager: PreferencesManager,
    private val modelRepository: ModelRepository,
    private val api: ApiInterface
) {

//    var isLoggedLiveData = MutableLiveData<Boolean>()
//    var productsList = MutableLiveData<MutableList<ProductModel>>()
//
//    fun register(login: String, pass: String): Single<LoginResponse> {
//        return api.register(login, pass)
//            .flatMap {
//                if (it.success) {
//                    prefManager.saveToken(it.token)
//                    isLoggedLiveData.postValue(true)
//                }
//                Single.just(it)
//            }
//    }

//    fun login(login: String, pass: String): LoginResult {
//        return api.login(login, pass)
//            .map {
//                it.toDomain()
//            }
//            .map {
//                if (it.success) {
//                    prefManager.saveToken(it.token)
//                    isLoggedLiveData.postValue(true)
//                }
//            }
//        return api.login(login, pass)
//        .observeOn(AndroidSchedulers.mainThread())
//            .doFinally { hideLoading() }
//            .autoDisposable(scope())
//            .subscribe({
//                success.invoke(it)
//            }, { error?.invoke(it) })
//    }

//    fun getProducts(): Single<MutableList<ProductModel>> {
//        return api.getProducts()
//            .flatMap {
//                productsList.postValue(it)
//                Single.just(it)
//            }
//    }
//
//    fun getReviews(productId: Int): Single<MutableList<ReviewModel>> {
//        return api.getReviews(productId)
//    }
//
//    fun isLogged(): Boolean {
//        return prefManager.getToken().isNotEmpty()
//    }
//
//    fun logout() {
//        prefManager.saveToken("")
//        prefManager.saveProfile(UserModel())
//        isLoggedLiveData.postValue(false)
//    }
//
//    fun saveProfile(user: UserModel) {
//        prefManager.saveProfile(user)
//    }
//
//    fun getProfile() = prefManager.getProfile()
//
//    fun postReview(post: PostReviewRequest, productId: Int): Single<PostReviewResponse> {
//        return api.postReview(
//            TOKEN_PREFIX + prefManager.getToken(),
//            CONTENT_TYPE_VALUE,
//            productId,
//            post
//        )
//    }
}
