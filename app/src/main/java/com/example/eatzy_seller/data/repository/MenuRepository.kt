package com.example.eatzy_seller.data.repository

//import com.example.eatzy_seller.data.local.MenuDao
import android.content.Context
import android.net.Uri
import androidx.room.Room
import com.example.eatzy_seller.data.local.AddOnCategoryEntity
import com.example.eatzy_seller.data.local.LocalDatabase
import com.example.eatzy_seller.data.local.MenuCategoryEntity
import com.example.eatzy_seller.data.local.MenuEntity
import com.example.eatzy_seller.data.model.AddOnCategory
import com.example.eatzy_seller.data.model.Menu
import com.example.eatzy_seller.data.model.MenuCategory
import com.example.eatzy_seller.data.model.RequestAddOnCategory
import com.example.eatzy_seller.data.model.UpdateAddonRequest
import com.example.eatzy_seller.data.model.UpdateMenuRequest
import com.example.eatzy_seller.data.model.User
import com.example.eatzy_seller.data.network.RetrofitClient
import com.example.eatzy_seller.data.network.api.MenuApiService
import com.example.eatzy_seller.ui.screen.menu.prepareFilePart
import retrofit2.Response

class MenuRepository(
    private val apiService: MenuApiService,
    private val db: LocalDatabase
    ) {
    suspend fun getMenus(token: String): List<MenuCategory> {
        return try {
            val response = apiService.getMenusWithAddOns(token)
            if (response.isSuccessful) {
                val data = response.body() ?: emptyList()

                // Clear old
                db.menuDao().clearCategories()
                db.menuDao().clearMenus()

                // Insert new
                val categoryEntities = data.map {
                    MenuCategoryEntity(it.idCategory ?: 0, it.categoryName ?: "")
                }
                val menuEntities = data.flatMap { category ->
                    category.menus?.map {
                        MenuEntity(
                            menuId = it.menuId ?: 0,
                            menuName = it.menuName ?: "",
                            menuPrice = it.menuPrice ?: 0.0,
                            menuPreparationTime = it.menuPreparationTime ?: 0,
                            menuAvailable = it.menuAvailable ?: true,
                            menuCategoryId = category.idCategory ?: 0
                        )
                    } ?: emptyList()
                }

                db.menuDao().insertCategories(categoryEntities)
                db.menuDao().insertMenus(menuEntities)

                data
            } else {
                loadMenusFromLocal()
            }
        } catch (e: Exception) {
            loadMenusFromLocal()
        }
    }

    private suspend fun loadMenusFromLocal(): List<MenuCategory> {
        return db.menuDao().getAllWithMenus().map {
            MenuCategory(
                idCategory = it.category.id,
                categoryName = it.category.name,
                canteenId = 4,
                menus = it.menus.map { m ->
                    Menu(
                        menuId = m.menuId,
                        menuName = m.menuName,
                        menuPrice = m.menuPrice,
                        menuPreparationTime = m.menuPreparationTime,
                        menuAvailable = m.menuAvailable,
                        menuImageRes = "", // tidak disimpan
                        listCategoryAddOn = null // tidak disimpan
                    )
                }
            )
        }
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

    suspend fun getMenuItem(token: String, id: Int): Response<Menu> {
        return apiService.getMenuItem(token, id)
    }

    suspend fun createCategory(token: String, newName: String): Boolean {
        return try {
            val response = apiService.createCategoryName(token,
                mapOf("menu_category_name" to newName)
            )
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

//    suspend fun upload(context: Context, uri: Uri) {
//        val imagePart = prepareFilePart(context, uri)
//        val response = apiService.uploadImage(imagePart)
//        if (response.isSuccessful) {
//            println("Berhasil upload: ${response.body()}")
//        } else {
//            println("Gagal upload: ${response.errorBody()?.string()}")
//        }
//    }


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
            val response = apiService.toggleAddonAvailability(token,id, mapOf("AddonAvailable" to isAvailable))
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

    suspend fun createAddonCategory(token: String, request: RequestAddOnCategory): Response<Unit> {
        return apiService.createAddonCategory(token, request)
    }

    suspend fun updateAddonCategory(token: String, id: Int?, addon: RequestAddOnCategory): Boolean {
        return try {
            val response = apiService.updateAddon(token, id, addon)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }


    //================== DAO ========================



}
