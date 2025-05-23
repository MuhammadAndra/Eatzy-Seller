package com.example.eatzy_seller.data.repository

//import com.example.eatzy_seller.data.local.MenuDao
import com.example.eatzy_seller.data.model.MenuCategory
import com.example.eatzy_seller.data.network.api.MenuApiService
import retrofit2.Response

class MenuRepository(private val apiService: MenuApiService) {
    suspend fun getMenus(token: String): Response<List<MenuCategory>> {
        return apiService.getMenusWithAddOns(token)
    }
}
