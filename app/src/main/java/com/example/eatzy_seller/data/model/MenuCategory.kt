package com.example.eatzy_seller.data.model

import com.google.gson.annotations.SerializedName

data class MenuCategory(
    @SerializedName("menu_category_id")
    val idCategory: Int,

    @SerializedName("canteen_id")
    val canteenId: Int,

    @SerializedName("menu_category_name")
    val categoryName: String,

    @SerializedName("menus")
    val menus: List<Menu>
)
