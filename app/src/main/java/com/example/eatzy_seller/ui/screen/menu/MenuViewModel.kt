package com.example.eatzy_seller.ui.screen.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatzy_seller.data.model.AddOnCategory
import com.example.eatzy_seller.data.model.MenuCategory
import com.example.eatzy_seller.data.network.RetrofitClient
import com.example.eatzy_seller.data.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MenuViewModel : ViewModel() {
    val token= "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NCwiZW1haWwiOiJidWtyaXNAZXhhbXBsZS5jb20iLCJyb2xlIjoiY2FudGVlbiIsImlhdCI6MTc0ODA3NTE3MywiZXhwIjoxNzQ4MDc4NzczfQ.W_IavJdfp0IlA8A8FXQPt51lYAd-hPp92jNgEFY_uiE"
    private val repository = MenuRepository(RetrofitClient.menuApi)

    private val _menuCategories = MutableStateFlow<List<MenuCategory>>(emptyList())
    val menuCategories: StateFlow<List<MenuCategory>> = _menuCategories

    private val _addonCategories = MutableStateFlow<List<AddOnCategory>>(emptyList())
    val addonCategories: StateFlow<List<AddOnCategory>> = _addonCategories


    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchMenus() {
        viewModelScope.launch {
            try {
                val response = repository.getMenus(token)
                if (response.isSuccessful) {
                    val body = response.body()
                    println("Response body: $body") // Log raw data
                    _menuCategories.value = body ?: emptyList()
                } else {
                    _error.value = "Gagal mengambil data: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }

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