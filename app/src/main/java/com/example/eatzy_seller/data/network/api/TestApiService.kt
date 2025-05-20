package com.example.eatzy_seller.data.network.api

import com.example.eatzy_seller.data.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface TestApiService {
    @GET("/users")
    suspend fun getUsersResponse(
        @Header("authorization") token: String
    ): Response<List<User>>

    //api test ambil all user versi pak aryo
//    @GET("/users")
//    fun getUsers(@Header("authorization") token: String): Call<List<User>>
}