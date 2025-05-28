package com.example.eatzy_seller.data.network.api

import com.example.eatzy_seller.data.model.AddOnCategory
import com.example.eatzy_seller.data.model.MenuCategory
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path


interface MenuApiService {
    @GET("menus/menu") // endpoint dari controller
    suspend fun getMenusWithAddOns(
        @Header("Authorization") token: String
    ): Response<List<MenuCategory>>

    @GET("menus/addon") // endpoint dari controller
    suspend fun getAddOnWithAddOns(
        @Header("Authorization") token: String
    ): Response<List<AddOnCategory>>

    @PUT("menus/categories")
    suspend fun updateCategoryName(
        @Header("Authorization") token: String,
        @Path("id") id: Int,//ganti ke body
        @Body body: Map<String, String>
    ): Response<Unit>

    @DELETE("menus/categories/{id}")
    suspend fun deleteCategory(
        @Header("Authorization") token: String,
        @Body id: Int
    ): Response<Unit>

    @DELETE("menus/deleteMenu/{id}")
    suspend fun deleteMenu(
        @Header("Authorization") token: String,
        @Body id: Int
    ): Response<Unit>

    @PATCH("menus/menus/{id}")
    suspend fun toggleMenuAvailability(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: Map<String, Boolean>
    ): Response<Unit>
}

