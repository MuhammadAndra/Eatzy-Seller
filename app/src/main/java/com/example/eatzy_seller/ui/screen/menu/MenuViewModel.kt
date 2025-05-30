package com.example.eatzy_seller.ui.screen.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatzy_seller.data.model.AddOnCategory
import com.example.eatzy_seller.data.model.MenuCategory
import com.example.eatzy_seller.data.model.UpdateMenuRequest
import com.example.eatzy_seller.data.network.RetrofitClient
import com.example.eatzy_seller.data.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// token
const val token = "bearer " +
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NCwiZW1haWwiOiJidWtyaXNAZXhhbXBsZS5jb20iLCJyb2xlIjoiY2FudGVlbiIsImlhdCI6MTc0ODU2NDc4MSwiZXhwIjoxNzY0MTE2NzgxfQ.UVXsenWc8EouRLgKZ9O8kpm_Ae2RP89jomX5-FWLYOQ"

class MenuViewModel : ViewModel() {
    //nyambung ke repository
    private val repository = MenuRepository(RetrofitClient.menuApi)

    //buat ambil semua kategori menu, addon
    private val _menuCategories = MutableStateFlow<List<MenuCategory>>(emptyList())
    val menuCategories: StateFlow<List<MenuCategory>> = _menuCategories

    //buat ambil semua addon
    private val _addonCategories = MutableStateFlow<List<AddOnCategory>>(emptyList())
    val addonCategories: StateFlow<List<AddOnCategory>> = _addonCategories

    //buat erro tapi belum dipake
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    //ambil semua kategori menu, menu, kategori addon, addon yang di menu
    fun fetchMenus() {
        viewModelScope.launch {
            try {
                val response = repository.getMenus(token)
                if (response.isSuccessful) {
                    val body = response.body()
                    println("Response body: $body") // Log raw data
                    _menuCategories.value = body ?: emptyList()
                    println(_menuCategories)
                } else {
                    _error.value = "Gagal mengambil data: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }

    fun updateCategoryName(categoryId: Int, newCategoryName: String) {
        viewModelScope.launch {
            Log.d("UpdateCategory", "New name: $newCategoryName")
            val success = repository.updateCategoryName(token, categoryId, newCategoryName)
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
            val success = repository.deleteCategory(token, categoryId)
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
            val success = repository.deleteMenu(token, menuId)
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
            val success = repository.toggleMenuAvailability(token, menuId, isAvailable)
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



    //===================Addon===================
    //ambil semua addon yang dipunya
    fun fetchAddons() {
        viewModelScope.launch {
            try {
                val response = repository.getAddons(token)
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



}