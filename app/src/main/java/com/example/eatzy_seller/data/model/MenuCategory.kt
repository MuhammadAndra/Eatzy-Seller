package com.example.eatzy_seller.data.model

data class MenuCategory(
    val idCategory: Int,
    val idCanteen : Int,
    val categoryName: String,
    val menus: List<Menu>
)
