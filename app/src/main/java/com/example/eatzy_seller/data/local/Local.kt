package com.example.eatzy_seller.data.local

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.RoomDatabase
import androidx.room.Transaction
import com.example.eatzy_seller.ui.screen.menu.MenuViewModel

@Entity(tableName = "menu_categories")
data class MenuCategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "menu_category_id")
    val id: Int,

    @ColumnInfo(name = "menu_category_name")
    val name: String
)

@Entity(tableName = "menus")
data class MenuEntity(
    @PrimaryKey val menuId: Int,
    val menuName: String,
    val menuPrice: Double,
    val menuPreparationTime: Int,
    val menuAvailable: Boolean,
    val menuCategoryId: Int // foreign key
)

@Entity(tableName = "addon_categories")
data class AddOnCategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "addon_category_id")
    val id: Int,

    @ColumnInfo(name = "addon_category_name")
    val name: String,

    @ColumnInfo(name = "is_multiple_choice")
    val isMultiple: Boolean
)

@Entity(tableName = "addons")
data class AddOnEntity(
    @PrimaryKey val addOnId: Int,
    val addOnName: String,
    val addOnPrice: Int,
    val addOnAvailable: Boolean,
    val addOnCategoryId: Int // foreign key
)

data class MenuCategoryWithMenus(
    @Embedded val category: MenuCategoryEntity,

    @Relation(
        parentColumn = "menu_category_id",
        entityColumn = "menuCategoryId"
    )
    val menus: List<MenuEntity>
)

data class AddOnCategoryWithAddOns(
    @Embedded val category: AddOnCategoryEntity,

    @Relation(
        parentColumn = "addon_category_id",
        entityColumn = "addOnCategoryId"
    )
    val addOns: List<AddOnEntity>
)


@Dao
interface MenuDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<MenuCategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenus(menus: List<MenuEntity>)

    @Query("DELETE FROM menu_categories")
    suspend fun clearCategories()

    @Query("DELETE FROM menus")
    suspend fun clearMenus()

    @Transaction
    @Query("SELECT * FROM menu_categories")
    suspend fun getAllWithMenus(): List<MenuCategoryWithMenus>
}


@Dao
interface AddOnDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<AddOnCategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddOns(addons: List<AddOnEntity>)

    @Query("DELETE FROM addon_categories")
    suspend fun clearCategories()

    @Query("DELETE FROM addons")
    suspend fun clearAddOns()

    @Transaction
    @Query("SELECT * FROM addon_categories")
    suspend fun getAllWithAddOns(): List<AddOnCategoryWithAddOns>
}

@Database(
    entities = [MenuCategoryEntity::class, MenuEntity::class, AddOnCategoryEntity::class, AddOnEntity::class],
    version = 1
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDao
    abstract fun addOnDao(): AddOnDao
}


