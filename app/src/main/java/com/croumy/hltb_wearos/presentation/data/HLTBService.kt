package com.croumy.hltb_wearos.presentation.data

import SubmitRequest
import com.croumy.hltb_wearos.presentation.models.api.GameListResponse
import com.croumy.hltb_wearos.presentation.models.api.GameRequest
import com.croumy.hltb_wearos.presentation.models.api.UserResponse
import com.croumy.hltb_wearos.presentation.models.api.UserResponseData
import com.croumy.hltbwearos.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HLTBService @Inject constructor(
    val preferencesService: PreferencesService
) {
    private val okHttpClient = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://howlongtobeat.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(AuthRestApi::class.java)

    interface AuthRestApi {
        @GET("user")
        suspend fun getUser(@HeaderMap headers: Map<String, String>): Response<UserResponseData>

        @POST("user/{userId}/games/list")
        suspend fun getGames(@Body request: GameRequest, @Path("userId") userId: Int): Response<GameListResponse>

        @POST("submit")
        suspend fun submitTime(@HeaderMap headers: Map<String, String>, @Body request: SubmitRequest): Response<Any>
    }

    suspend fun getUser(): UserResponse? {
        val response = retrofit.getUser(
            headers = mapOf("Cookie" to preferencesService.token!!,)
        )

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.data[0]
        } else {
            throw Exception(response.message())
        }
    }


    suspend fun getGames(gameRequest: GameRequest = GameRequest()): GameListResponse? {
        val response = retrofit.getGames(gameRequest, preferencesService.userId!!)

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception(response.message())
        }
    }

    suspend fun submitTime(submitRequest: SubmitRequest) {
        val response = retrofit.submitTime(
            headers = mapOf("Cookie" to preferencesService.token!!,),
            request = submitRequest)

        if (!response.isSuccessful) {
            throw Exception(response.message())
        } else {
            print(response.body())
        }
    }
}