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
                                name = category.name,
                                menus = category.menus.map { menu ->
                                    Menu(
                                        title = menu.title,
                                        price = menu.price,
                                        imageRes = menu.imageRes,
                                        visibleMenu = menu.visibleMenu
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
}
