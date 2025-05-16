package com.example.eatzy_seller.ui.screen.menu

import androidx.lifecycle.ViewModel
import com.example.eatzy_seller.data.model.Menu
import com.example.eatzy_seller.data.model.MenuCategory
import com.example.eatzy_seller.data.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuViewModel : ViewModel() {

    private val _menuCategories = MutableStateFlow<List<MenuCategory>>(emptyList())
    val menuCategories: StateFlow<List<MenuCategory>> = _menuCategories

    fun fetchMenus() {
        RetrofitClient.menuApi.getMenusWithCategories()
            .enqueue(object : Callback<List<MenuCategory>> {
                override fun onResponse(
                    call: Call<List<MenuCategory>?>?,
                    response: Response<List<MenuCategory>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body() ?: emptyList()
                        _menuCategories.value = result.map { category ->
                            MenuCategory(
                                idCategory = category.idCategory,
                                idCanteen = category.idCanteen,
                                categoryName = category.categoryName,
                                menus = category.menus.map { menu ->
                                    Menu(
                                        menuId = menu.menuId,
                                        menuName = menu.menuName,
                                        menuPrice = menu.menuPrice,
                                        menuImageRes = menu.menuImageRes,
                                        menuAvailable = menu.menuAvailable
                                    )
                                }
                            )
                        }
                    }
                }

                override fun onFailure(
                    call: Call<List<MenuCategory>?>?,
                    t: Throwable?
                ) {
                }

            })

    }

    fun deleteMenu(menuId: Int) {
        RetrofitClient.menuApi.deleteMenu(menuId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    // Update local state
                    _menuCategories.value = _menuCategories.value.map { category ->
                        category.copy(
                            menus = category.menus.filterNot { it.menuId == menuId }
                        )
                    }
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // Bisa tampilkan error kalau mau
            }
        })
    }

}
