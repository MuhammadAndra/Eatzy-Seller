package com.example.eatzy_seller.data.model

import com.google.gson.annotations.SerializedName

data class AddOnCategory(
    @SerializedName("addon_category_id")
    val addOnCategoryId : Int,

    @SerializedName("addon_category_name")
    val addOnCategoryName: String,

    @SerializedName("is_multiple_choice")
    val addOnCategoryMultiple : Boolean,

    @SerializedName("addons")
    val addOns: List<AddOn>
)
