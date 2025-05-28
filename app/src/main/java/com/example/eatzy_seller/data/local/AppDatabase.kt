package com.example.eatzy_seller.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.eatzy_seller.data.local.OrderDao
import com.example.eatzy_seller.data.local.OrderStateEntity

@Database(entities = [OrderStateEntity::class, OrderItemEntity::class], version = 1)
@TypeConverters(OrderItemListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "eatzy"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
