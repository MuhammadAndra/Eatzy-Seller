package com.example.eatzy_seller.data.network.api

import com.example.eatzy_seller.data.model.AddOnCategory
import com.example.eatzy_seller.data.model.MenuCategory
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path


interface MenuApiService {
    @GET("menus/menu") // endpoint dari controller
    suspend fun getMenusWithAddOns(
        @Header("Authorization") token: String
    ): Response<List<MenuCategory>>
}

