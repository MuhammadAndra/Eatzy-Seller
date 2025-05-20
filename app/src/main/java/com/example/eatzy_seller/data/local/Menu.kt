package com.example.eatzy_seller.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu")
data class Menu(
    @PrimaryKey val menuId: Int,
    val menuName: String,
    val menuPrice: Double,
    val categoryId: Int
)

@Entity(tableName = "addon")
data class Addon(
    @PrimaryKey val addonId: Int,
    val addonName: String,
    val addonPrice: Double,
    val categoryId: Int
)

@Entity(tableName = "menu_category")
data class MenuCategory(
    @PrimaryKey val idCategory: Int,
    val categoryName: String,
    val idCanteen: Int
)

@Entity(tableName = "addon_category")
data class AddonCategory(
    @PrimaryKey val idCategory: Int,
    val categoryName: String
)

