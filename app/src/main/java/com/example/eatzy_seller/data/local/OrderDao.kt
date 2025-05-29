package com.example.eatzy_seller.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.eatzy_seller.data.model.OrderItem
import com.example.eatzy_seller.data.model.OrderState
import kotlinx.coroutines.flow.Flow

//@Dao
//interface OrderDao {
//    //untuk menggabungkan data dari dua tabel (order dan order_items)
//    @Transaction
//    @Query("SELECT * FROM orders WHERE order_status = :status")
//    fun getOrdersByStatus(status: String): Flow<List<OrderStateEntity>>
//
//    //simpan daftar order ke database (replace kalau sdh ada)
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertOrderItems(orderItems: List<OrderStateEntity>)
//
//    //update status
//    @Query("UPDATE orders SET order_status = :newStatus WHERE order_id = :orderId")
//    suspend fun updateStatus(orderId: Int, newStatus: String)
//}

