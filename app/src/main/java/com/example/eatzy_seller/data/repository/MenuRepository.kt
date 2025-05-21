package com.example.eatzy_seller.data.repository

//import com.example.eatzy_seller.data.local.MenuDao
import com.example.eatzy_seller.data.model.*
import com.example.eatzy_seller.data.network.api.MenuApiService
//import com.example.eatzy_seller.data.remote.MenuApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MenuRepository(
    private val api: MenuApiService,
    //private val dao: MenuDao
) {
    // GET from API
    suspend fun getMenus(): List<MenuCategory> = withContext(Dispatchers.IO) {
        try {
            val response = api.getMenusWithCategories()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else emptyList()
        } catch (e: Exception) {
            emptyList() // Or fallback from Room if desired
        }
    }

    suspend fun getAddons(): List<AddOnCategory> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAddonsWithCategories()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

//    // INSERT
//    suspend fun insertMenu(menu: Menu) = withContext(Dispatchers.IO) {
//        dao.insertMenu(menu)
//    }
//
//    suspend fun insertAddon(addon: AddOn) = withContext(Dispatchers.IO) {
//        dao.insertAddon(addon)
//    }
//
//    suspend fun insertMenuCategory(category: MenuCategory) = withContext(Dispatchers.IO) {
//        dao.insertMenuCategory(category)
//    }
//
//    suspend fun insertAddonCategory(category: AddOnCategory) = withContext(Dispatchers.IO) {
//        dao.insertAddonCategory(category)
//    }
//
//    // UPDATE
//    suspend fun updateMenu(menu: Menu) = withContext(Dispatchers.IO) {
//        dao.updateMenu(menu)
//    }
//
//    suspend fun updateAddon(addon: AddOn) = withContext(Dispatchers.IO) {
//        dao.updateAddon(addon)
//    }
//
//    // DELETE
//    suspend fun deleteMenu(menuId: Int) = withContext(Dispatchers.IO) {
//        dao.deleteMenu(menuId)
//    }
//
//    suspend fun deleteAddon(addonId: Int) = withContext(Dispatchers.IO) {
//        dao.deleteAddon(addonId)
//    }
//
//    suspend fun deleteMenuCategory(categoryId: Int) = withContext(Dispatchers.IO) {
//        dao.deleteMenuCategory(categoryId)
//    }
//
//    suspend fun deleteAddonCategory(categoryId: Int) = withContext(Dispatchers.IO) {
//        dao.deleteAddonCategory(categoryId)
//    }
}
