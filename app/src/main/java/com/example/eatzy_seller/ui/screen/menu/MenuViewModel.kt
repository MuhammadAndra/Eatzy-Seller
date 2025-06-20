package com.example.eatzy_seller.ui.screen.menu

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.eatzy_seller.data.local.LocalDatabase
import com.example.eatzy_seller.data.model.AddOnCategory
import com.example.eatzy_seller.data.model.Menu
import com.example.eatzy_seller.data.model.MenuCategory
import com.example.eatzy_seller.data.model.RequestAddOnCategory
import com.example.eatzy_seller.data.model.UpdateAddonRequest
import com.example.eatzy_seller.data.model.UpdateMenuRequest
import com.example.eatzy_seller.data.network.RetrofitClient
import com.example.eatzy_seller.data.repository.MenuRepository
import com.example.eatzy_seller.token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// token

class MenuViewModel(application: Application) : AndroidViewModel(application) {
    //nyambung ke repository
    private val context = application.applicationContext

    val dbl = Room.databaseBuilder(
        context,
        LocalDatabase::class.java,
        "eatzy.db"
    ).build()

    //nyambung ke repository
    private val repository = MenuRepository(
        RetrofitClient.menuApi,
        db = dbl)
    //buat ambil semua kategori menu, addon
    private val _menuCategories = MutableStateFlow<List<MenuCategory>>(emptyList())
    val menuCategories: StateFlow<List<MenuCategory>> = _menuCategories

    //buat ambil semua addon
    private val _addonCategories = MutableStateFlow<List<AddOnCategory>>(emptyList())
    val addonCategories: StateFlow<List<AddOnCategory>> = _addonCategories

    val tokenUser = "Bearer "+ token



    //buat erro tapi belum dipake
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    //ambil semua kategori menu, menu, kategori addon, addon yang di menu
    fun fetchMenus() {
        viewModelScope.launch {
            try {
                val response = repository.getMenus(tokenUser)
                _menuCategories.value = response
            } catch (e: Exception) {
                _error.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }

    fun updateCategoryName(categoryId: Int, newCategoryName: String) {
        viewModelScope.launch {
            Log.d("UpdateCategory", "New name: $newCategoryName")
            val success = repository.updateCategoryName(tokenUser, categoryId, newCategoryName)
            if (success) {
                _menuCategories.update { list ->
                    list.map {
                        if (it.idCategory == categoryId) it.copy(categoryName = newCategoryName)
                        else it
                    }
                }
                //Log.d("UpdateCategory", "New name: $newCategoryName")
            }
        }
    }

    fun deleteCategory(categoryId: Int) {
        viewModelScope.launch {
            val success = repository.deleteCategory(tokenUser, categoryId)
            Log.d("id","$categoryId")
            if (success) {
                _menuCategories.update { list ->
                    list.filter { it.idCategory != categoryId }
                }
            }
        }
    }

    fun deleteMenu(menuId: Int) {
        viewModelScope.launch {
            val success = repository.deleteMenu(tokenUser, menuId)
            if (success) {
                _menuCategories.update { list ->
                    list.map { category ->
                        category.copy(
                            menus = category.menus?.filter { it.menuId != menuId } ?: emptyList()
                        )                    }
                }
            }
        }
    }

    fun toggleMenuAvailability(menuId: Int, isAvailable: Boolean) {
        viewModelScope.launch {
            val success = repository.toggleMenuAvailability(tokenUser, menuId, isAvailable)
            if (success) {
                _menuCategories.update { list ->
                    list.map { category ->
                        category.copy(
                            menus = category.menus?.map {
                                if (it.menuId == menuId) it.copy(menuAvailable = isAvailable)
                                else it
                            } ?: emptyList()
                        )
                    }
                }
            }
        }
    }

    fun updateMenu(menuId: Int, updatedMenu: UpdateMenuRequest) {
        viewModelScope.launch {
            val success = repository.updateMenu(token, menuId, updatedMenu)
            if (success) {
                fetchMenus()
            } else {
                _error.value = "Gagal mengupdate menu."
            }
        }
    }

//    fun fetchMenuItem(menuId: Int){
//        viewModelScope.launch {
//            val success = repository.getMenuItem(token, menuId)
//            if (success) {
//                fetchMenus()
//            } else {
//                _error.value = "Gagal mengambil menu."
//            }
//        }
//    }




    fun createCategory( newCategoryName: String) {
        viewModelScope.launch {
            Log.d("CreateCategory", "New name: $newCategoryName")
            val success = repository.createCategory(tokenUser, newCategoryName)
            if (success) {
                fetchMenus()
                //Log.d("CreateCategory", "New name: $newCategoryName")
            }
        }
    }

    //===================Addon===================
    //ambil semua addon yang dipunya
    fun fetchAddons() {
        viewModelScope.launch {
            try {
                val response = repository.getAddons(tokenUser)
                if (response.isSuccessful) {
                    val body = response.body()
                    println("Response body: $body") // Log raw data
                    _addonCategories.value = body ?: emptyList()
                } else {
                    _error.value = "Gagal mengambil data: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }

    fun deleteAddonCategory(categoryId: Int) {
        viewModelScope.launch {
            val success = repository.deleteAddonCategory(tokenUser, categoryId)
            Log.d("id","$categoryId")
            if (success) {
                _addonCategories.update { list ->
                    list.filter { it.addOnCategoryId != categoryId }
                }
            }
        }
    }

    fun deleteAddon(addonId: Int) {
        viewModelScope.launch {
            val success = repository.deleteAddon(tokenUser, addonId)
            if (success) {
                _addonCategories.update { list ->
                    list.map { category ->
                        category.copy(
                            addOns = category.addOns?.filter { it.AddOnId != addonId } ?: emptyList()
                        )                    }
                }
            }
        }
    }

    fun toggleAddonAvailability(addonId: Int, isAvailable: Boolean) {
        viewModelScope.launch {
            val success = repository.toggleAddonAvailability(tokenUser, addonId, isAvailable)
            if (success) {
                _addonCategories.update { list ->
                    list.map { category ->
                        category.copy(
                            addOns = category.addOns?.map {
                                if (it.AddOnId == addonId) it.copy(AddOnAvailable = isAvailable)
                                else it
                            } ?: emptyList()
                        )
                    }
                }
            }
        }
    }

    fun updateAddon(menuId: Int, updatedAddon: UpdateAddonRequest) {
        viewModelScope.launch {
            val success = repository.updateAddonItem(tokenUser, menuId, updatedAddon)
            if (success) {
                fetchAddons()
            } else {
                _error.value = "Gagal mengupdate menu."
            }
        }
    }

    fun createAddonCategory(request: RequestAddOnCategory) {
        viewModelScope.launch {
            try {
                val response = repository.createAddonCategory(tokenUser, request)
                if (response.isSuccessful ) {
                    fetchAddons() // Refresh data setelah tambah
                } else {
                    _error.value = "Gagal membuat kategori add-on"
                }
            } catch (e: Exception) {
            }
        }
    }

    fun updateAddonCategory(menuId: Int?, updatedAddon: RequestAddOnCategory) {
        viewModelScope.launch {
            val success = repository.updateAddonCategory(tokenUser, menuId, updatedAddon)
            if (success) {
                fetchAddons()
            } else {
                _error.value = "Gagal mengupdate menu."
            }
        }
    }

}