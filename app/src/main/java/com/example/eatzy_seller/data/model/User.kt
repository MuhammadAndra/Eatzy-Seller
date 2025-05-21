package com.example.eatzy_seller.data.model

data class User(
    //contoh dataclass silahkan edit jika perlu
    val name:String="",
    val email:String="",
    val canteenId:Int= 1,
    val menuCategories: List<MenuCategory>,
    val addOnCategories: List<AddOnCategory>
)
