package com.example.eatzy_seller.data.model

data class AddOnCategory(
    val addOnCategoryId : Int,
    //val kantinId: Int,
    val addOnCategoryName: String,
    val addOnCategoryMultiple : Boolean,
    val addOns: List<AddOn>
)
