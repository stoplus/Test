package com.test.network

import com.test.network.models.data.response.LoginResponse
import com.test.network.models.data.response.ProductResponse
import com.test.network.models.ReviewModel
import com.test.network.models.data.request.PostReviewRequest
import com.test.network.models.data.response.PostReviewResponse
import com.test.network.models.data.response.RegisterResponse
import com.test.utils.CONTENT_TYPE_NAME
import com.test.utils.TOKEN_NAME
import io.reactivex.Single
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("api/register/")
    fun register(
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<RegisterResponse>

    @FormUrlEncoded
    @POST("api/login/")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<LoginResponse>

    @GET("api/products/")
    fun getProducts(): Single<MutableList<ProductResponse>>

    @GET("api/reviews/{productId}")
    fun getReviews(@Path("productId") productId: Int): Single<MutableList<ReviewModel>>

    @POST("api/reviews/{productId}")
    fun postReview(
        @Header(TOKEN_NAME) token: String,
        @Header(CONTENT_TYPE_NAME) contentType: String,
        @Path("productId") productId: Int,
        @Body body: PostReviewRequest
    ): Single<PostReviewResponse>
}