package com.example.eatzy_seller.data.network.api

import com.example.eatzy_seller.data.model.MenuCategory
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface MenuApiService {
    @GET("menus/with-categories")
    fun getMenusWithCategories(): Call<List<MenuCategory>>

    @PATCH("menu/{id}/visibility")
    fun toggleMenuVisibility(@Path("id") menuId: Int): Call<Unit>

    @DELETE("menu/{id}")
    fun deleteMenu(@Path("id") menuId: Int): Call<Unit>
}

