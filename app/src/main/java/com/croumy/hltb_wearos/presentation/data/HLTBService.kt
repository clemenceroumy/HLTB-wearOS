package com.croumy.hltb_wearos.presentation.data

import com.croumy.hltb_wearos.presentation.models.GameListResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory

class HLTBService {
    private val okHttpClient = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://howlongtobeat.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(AuthRestApi::class.java)

    interface AuthRestApi {
        @GET("user/304670/games/list")
        suspend fun getGames(): Response<GameListResponse>
    }


    suspend fun getGames(): GameListResponse? {
        val response = retrofit.getGames()

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception(response.message())
        }
    }
}