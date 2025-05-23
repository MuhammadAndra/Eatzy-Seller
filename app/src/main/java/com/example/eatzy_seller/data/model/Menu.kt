package com.example.eatzy_seller.data.model

import com.google.gson.annotations.SerializedName

data class Menu(
    @SerializedName("menu_id")
    val menuId: Int,

    @SerializedName("menu_name")
    val menuName: String,

    @SerializedName("menu_price")
    val menuPrice: Double,

    @SerializedName("preparation_time")
    val menuPreparationTime: Int,

    @SerializedName("menu_image")
    val menuImageRes: String,

    @SerializedName("menu_is_available")
    val menuAvailable: Boolean,

    @SerializedName("addon_categories")
    val listCategoryAddOn: List<AddOnCategory>?
)
