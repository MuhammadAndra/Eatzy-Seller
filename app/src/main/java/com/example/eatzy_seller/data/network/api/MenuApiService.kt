package com.example.eatzy_seller.data.network.api

import com.example.eatzy_seller.data.model.AddOnCategory
import com.example.eatzy_seller.data.model.MenuCategory
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface MenuApiService {
    @GET("menus")
    suspend fun getMenusWithCategories(): Response<List<MenuCategory>>

    @GET("addons")
    suspend fun getAddonsWithCategories(): Response<List<AddOnCategory>>
}

