package com.example.eatzy_seller.data.model
import androidx.room.Entity import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    //contoh dataclass silahkan edit jika perlu
    @PrimaryKey
    val userId: Int,
    val name:String="",
    val email:String="",
    val canteenId:Int= 1,
    val menuCategories: List<MenuCategory>,
    val addOnCategories: List<AddOnCategory>
    val password: String,
    val isVerified: Boolean,
    val createdAt: String,
    val updatedAt: String
)
