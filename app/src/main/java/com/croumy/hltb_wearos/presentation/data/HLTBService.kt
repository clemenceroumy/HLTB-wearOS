package com.croumy.hltb_wearos.presentation.data

import com.croumy.hltb_wearos.presentation.models.api.GameListResponse
import com.croumy.hltb_wearos.presentation.models.api.GameRequest
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class HLTBService {
    private val okHttpClient = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://howlongtobeat.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(AuthRestApi::class.java)

    interface AuthRestApi {
        @POST("user/304670/games/list")
        suspend fun getGames(@Body request: GameRequest): Response<GameListResponse>
    }


    suspend fun getGames(gameRequest: GameRequest): GameListResponse? {
        val response = retrofit.getGames(gameRequest)

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception(response.message())
        }
    }
}