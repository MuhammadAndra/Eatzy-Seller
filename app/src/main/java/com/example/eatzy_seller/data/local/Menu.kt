//package com.example.eatzy_seller.data.local
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//
//@Entity(tableName = "menus")
//data class Menu(
//    @PrimaryKey(autoGenerate = true) val menuId: Int,
//    val menuCategoryId: Int,
//    val menuName: String,
//    val menuPrice: Double,
//    val menuIsAvailable: Boolean
//
//)
//
//
//@Entity(tableName = "addons")
//data class Addon(
//    @PrimaryKey(autoGenerate = true) val addonId: Int,
//    val addonCategoryId: Int,
//    val addonIsAvailable: Boolean,
//    val addonName: String,
//    val addonPrice: Double
//)
//
//@Entity(tableName = "menu_categories")
//data class MenuCategory(
//    @PrimaryKey(autoGenerate = true) val menuCategoryId: Int,
//    val canteenId: Int,
//    val menuCategoryName: String
//)
//
//@Entity(tableName = "addon_categories")
//data class AddonCategory(
//    @PrimaryKey(autoGenerate = true) val addonCategoryId: Int,
//    val canteenId: Int,
//    val addonCategoryName: String,
//    val isMultipleChoice: Boolean
//)
//
////kalau many to many
//@Entity(
//    tableName = "menu_addon_categories",
//    primaryKeys = ["menuId", "addonCategoryId"]
//)
//data class MenuAddonCrossRef(
//    val menuId: Int,
//    val addonCategoryId: Int
//)
