package com.example.eatzy_seller.data.network.api

import com.example.eatzy_seller.data.model.AddOnCategory
import com.example.eatzy_seller.data.model.Menu
import com.example.eatzy_seller.data.model.MenuCategory
import com.example.eatzy_seller.data.model.UpdateAddonRequest
import com.example.eatzy_seller.data.model.UpdateMenuRequest
import okhttp3.RequestBody
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

    @PUT("menus/categories/{id}")
    suspend fun updateCategoryName(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: Map<String, String>
    ): Response<Unit>

    @DELETE("menus/deleteCategoriesMenu/{id}")
    suspend fun deleteCategory(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @DELETE("menus/deleteMenu/{id}")
    suspend fun deleteMenu(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @PUT("menus/availableMenu/{id}")
    suspend fun toggleMenuAvailability(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: Map<String, Boolean>
    ): Response<Unit>

    //update menu
    @PUT("menus/menus/{id}")
    suspend fun updateMenu(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: UpdateMenuRequest
    ): Response<Unit>

    @GET("menus/menuItem/{id}")
    suspend fun getMenuItem(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Menu>

    @GET("menus/categoryList")
    suspend fun getMenuCategories(
        @Header("Authorization") token: String
    ):  Response<List<MenuCategory>>

    @GET("menus/categoryAddonList")
    suspend fun getAddonCategories(
        @Header("Authorization") token: String
    ):  Response<List<AddOnCategory>>




    //===================Addon===================
    @GET("menus/addon") // endpoint dari controller
    suspend fun getAddOnWithAddOns(
        @Header("Authorization") token: String
    ): Response<List<AddOnCategory>>

    @DELETE("menus/deleteCategoriesAddon/{id}")
    suspend fun deleteAddonCategory(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @DELETE("menus/deleteAddons/{id}")
    suspend fun deleteAddon(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @PUT("menus/availableAddon/{id}")
    suspend fun toggleAddonAvailability(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: Map<String, Boolean>
    ): Response<Unit>

    @PATCH("menus/updateAddon/{id}")
    suspend fun updateAddonItem(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body body: UpdateAddonRequest
    ): Response<Unit>
}

