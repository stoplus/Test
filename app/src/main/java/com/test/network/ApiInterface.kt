package com.test.network

import com.test.network.models.response.LoginResponse
import com.test.network.models.ProductModel
import com.test.network.models.ReviewModel
import com.test.network.models.request.PostReviewRequest
import com.test.network.models.response.PostReviewResponse
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
    ): Single<LoginResponse>

    @FormUrlEncoded
    @POST("api/login/")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<LoginResponse>

    @GET("api/products/")
    fun getProducts(): Single<MutableList<ProductModel>>

    @GET("api/reviews/{productId}")
    fun getReviews(@Path("productId") productId: Int): Single<MutableList<ReviewModel>>

    @POST("api/reviews/{productId}")
    fun postReview(
        @Header(TOKEN_NAME) token: String,
        @Header(CONTENT_TYPE_NAME) contentType: String,
        @Path("productId") productId: Int,
        @Body body: PostReviewRequest
    ): Single<PostReviewResponse>
//
//    @FormUrlEncoded
//    @POST("test/agents/betslips")
//    fun sprotBettingPlaceBet(
//        @Query("api_key") apiKey: String,
//        @FieldMap events: HashMap<String, String>,
//        @Field("testData[agent_token]") token: String
//    ): Single<StatusBetslip>
//
//    @FormUrlEncoded
//    @POST("test/agent_profile")
//    fun getAgentProfile(
//        @Query("api_key") apiKey: String,
//        @Field("test_agent_profile[instance]") instance: String,
//        @Field("test_agent_profile[token]") token: String
//    ): Single<AgentProfileRest>
//
//    @GET("public/betslips/{public_code}")
//    fun publicBetslips(
//        @Path("public_code")
//        publicCode: String
//    ): Single<BetLookupObj>
}