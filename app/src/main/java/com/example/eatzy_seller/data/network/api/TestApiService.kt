package com.example.eatzy_seller.data.network.api

import com.example.eatzy_seller.data.model.User
import retrofit2.Call
import retrofit2.http.GET

interface TestApiService {
    //api test ambil all user
    @GET("/users")
    fun getUsers(): Call<List<User>>
}