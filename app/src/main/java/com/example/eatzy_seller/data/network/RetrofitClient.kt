package com.example.eatzy_seller.data.network

import com.example.eatzy_seller.data.network.api.MenuApiService
import com.example.eatzy_seller.data.network.api.TestApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASEURL = "http://10.0.2.2:3002/"
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttpClient = OkHttpClient.Builder().addInterceptor(
        loggingInterceptor
    ).build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val testApi: TestApiService by lazy { retrofit.create(TestApiService::class.java) }

    //API Menu
    val menuApi: MenuApiService by lazy { retrofit.create(MenuApiService::class.java) }


}