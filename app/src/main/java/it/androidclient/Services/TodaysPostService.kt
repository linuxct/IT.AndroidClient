package it.androidclient.Services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface TodaysPostService {
    @GET("TodaysPost")
    suspend fun todaysPost() : TodaysPostModel.Result

    companion object {
        fun create(): TodaysPostService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://itbff-pre.linuxct.space/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(TodaysPostService::class.java)
        }
    }
}