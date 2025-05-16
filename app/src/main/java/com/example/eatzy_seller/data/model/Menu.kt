package com.example.eatzy_seller.data.model

data class Menu(
    val menuId : Int,
    val menuName: String,
    val menuPrice: Double,
    //val menuPreparationTime: String,
    val menuImageRes: String,
    val menuAvailable: Boolean,
    //val listCategoryAddOn: List<AddOnCategory>
)
