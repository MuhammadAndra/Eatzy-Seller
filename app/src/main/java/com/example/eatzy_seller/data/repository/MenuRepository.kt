package com.example.eatzy_seller.data.repository

//import com.example.eatzy_seller.data.local.MenuDao
import com.example.eatzy_seller.data.model.AddOnCategory
import com.example.eatzy_seller.data.model.MenuCategory
import com.example.eatzy_seller.data.model.UpdateAddonRequest
import com.example.eatzy_seller.data.model.UpdateMenuRequest
import com.example.eatzy_seller.data.network.api.MenuApiService
import retrofit2.Response

class MenuRepository(private val apiService: MenuApiService) {
    suspend fun getMenus(token: String): Response<List<MenuCategory>> {
        return apiService.getMenusWithAddOns(token)
    }

    suspend fun updateCategoryName(token: String, id: Int, newName: String): Boolean {
        return try {
            val response = apiService.updateCategoryName(token, id,
                mapOf("menu_category_name" to newName)
            )
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteCategory(token: String, id: Int): Boolean {
        return try {
            val response = apiService.deleteCategory(token, id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteMenu(token: String, id: Int): Boolean {
        return try {
            val response = apiService.deleteMenu(token,id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun toggleMenuAvailability(token: String, id: Int, isAvailable: Boolean): Boolean {
        return try {
            val response = apiService.toggleMenuAvailability(token,id, mapOf("menuAvailable" to isAvailable))
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateMenu(token: String, id: Int, menu: UpdateMenuRequest): Boolean {
        return try {
            val response = apiService.updateMenu(token, id, menu)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getMenuItem(token: String, id: Int): Boolean {
        return try {
            val response = apiService.getMenuItem(token, id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }



    //===================Addon===================
    suspend fun getAddons(token: String): Response<List<AddOnCategory>> {
        return apiService.getAddOnWithAddOns(token)
    }

    suspend fun deleteAddonCategory(token: String, id: Int): Boolean {
        return try {
            val response = apiService.deleteAddonCategory(token, id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteAddon(token: String, id: Int): Boolean {
        return try {
            val response = apiService.deleteAddon(token,id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun toggleAddonAvailability(token: String, id: Int, isAvailable: Boolean): Boolean {
        return try {
            val response = apiService.toggleAddonAvailability(token,id, mapOf("menuAvailable" to isAvailable))
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateAddonItem(token: String, id: Int, addon: UpdateAddonRequest): Boolean {
        return try {
            val response = apiService.updateAddonItem(token, id, addon)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
