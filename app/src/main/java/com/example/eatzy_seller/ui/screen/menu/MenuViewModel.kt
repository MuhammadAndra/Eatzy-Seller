package com.example.eatzy_seller.ui.screen.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatzy_seller.data.model.*
import com.example.eatzy_seller.data.repository.MenuRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MenuViewModel(
    private val repository: MenuRepository
) : ViewModel() {

    // Menu Category
    private val _menuCategories = MutableStateFlow<List<MenuCategory>>(emptyList())
    val menuCategories: StateFlow<List<MenuCategory>> = _menuCategories

    // Addon Category
    private val _addonCategories = MutableStateFlow<List<AddOnCategory>>(emptyList())
    val addonCategories: StateFlow<List<AddOnCategory>> = _addonCategories

    // Fetch from API
    fun fetchAllMenus() {
        viewModelScope.launch {
            val result = repository.getMenus()
            _menuCategories.value = result
        }
    }

    fun fetchAllAddons() {
        viewModelScope.launch {
            val result = repository.getAddons()
            _addonCategories.value = result
        }
    }

    // Insert
    fun insertMenu(menu: Menu) = viewModelScope.launch {
        repository.insertMenu(menu)
        fetchAllMenus()
    }

    fun insertAddon(addon: AddOn) = viewModelScope.launch {
        repository.insertAddon(addon)
        fetchAllAddons()
    }

    fun insertMenuCategory(category: MenuCategory) = viewModelScope.launch {
        repository.insertMenuCategory(category)
        fetchAllMenus()
    }

    fun insertAddonCategory(category: AddOnCategory) = viewModelScope.launch {
        repository.insertAddonCategory(category)
        fetchAllAddons()
    }

    // Update
    fun updateMenu(menu: Menu) = viewModelScope.launch {
        repository.updateMenu(menu)
        fetchAllMenus()
    }

    fun updateAddon(addon: AddOn) = viewModelScope.launch {
        repository.updateAddon(addon)
        fetchAllAddons()
    }

    // Delete
    fun deleteMenu(menuId: Int) = viewModelScope.launch {
        repository.deleteMenu(menuId)
        fetchAllMenus()
    }

    fun deleteAddon(addonId: Int) = viewModelScope.launch {
        repository.deleteAddon(addonId)
        fetchAllAddons()
    }

    fun deleteMenuCategory(categoryId: Int) = viewModelScope.launch {
        repository.deleteMenuCategory(categoryId)
        fetchAllMenus()
    }

    fun deleteAddonCategory(categoryId: Int) = viewModelScope.launch {
        repository.deleteAddonCategory(categoryId)
        fetchAllAddons()
    }
}
