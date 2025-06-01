//package com.example.eatzy_seller.data.local
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import androidx.room.Update
//import com.example.eatzy_seller.data.model.AddOn
//import com.example.eatzy_seller.data.model.AddOnCategory
//import com.example.eatzy_seller.data.model.Menu
//import com.example.eatzy_seller.data.model.MenuCategory
//
//@Dao
//interface MenuDao {
//    // Menu
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertMenu(menu: Menu)
//
//    @Update
//    suspend fun updateMenu(menu: Menu)
//
//    @Query("DELETE FROM menu WHERE menuId = :menuId")
//    suspend fun deleteMenu(menuId: Int)
//
//    // Addon
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAddon(addon: Addon)
//
//    @Update
//    suspend fun updateAddon(addon: Addon)
//
//    @Query("DELETE FROM addon WHERE addonId = :addonId")
//    suspend fun deleteAddon(addonId: Int)
//
//    // Menu Category
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertMenuCategory(category: MenuCategory)
//
//    @Query("DELETE FROM menu_category WHERE idCategory = :categoryId")
//    suspend fun deleteMenuCategory(categoryId: Int)
//
//    // Addon Category
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAddonCategory(category: AddonCategory)
//
//    @Query("DELETE FROM addon_category WHERE idCategory = :categoryId")
//    suspend fun deleteAddonCategory(categoryId: Int)
//
//
////    // Insert
////    @Insert
////    suspend fun insertMenu(menu: Menu)
////
////    @Insert
////    suspend fun insertAddon(addon: AddOn)
////
////    @Insert
////    suspend fun insertMenuCategory(category: MenuCategory)
////
////    @Insert
////    suspend fun insertAddonCategory(category: AddOnCategory)
////
////    // Update
////    @Update suspend fun updateMenu(menu: Menu)
////
////    @Update suspend fun updateAddon(addon: AddOn)
////
////    // Delete
////    @Query("DELETE FROM menu WHERE menuId = :menuId") suspend fun deleteMenu(menuId: Int)
////    @Query("DELETE FROM addon WHERE addonId = :addonId") suspend fun deleteAddon(addonId: Int)
////    @Query("DELETE FROM menu_category WHERE idCategory = :categoryId") suspend fun deleteMenuCategory(categoryId: Int)
////    @Query("DELETE FROM addon_category WHERE idCategory = :categoryId") suspend fun deleteAddonCategory(categoryId: Int)
//}
