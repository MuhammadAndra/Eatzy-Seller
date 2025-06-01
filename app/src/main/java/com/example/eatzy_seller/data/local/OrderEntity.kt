package com.example.eatzy_seller.data.local

import androidx.room.Embedded
import com.example.eatzy_seller.data.model.OrderItem
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters

//@Entity(tableName = "orders")
//@TypeConverters(OrderItemListConverter::class)
//data class OrderStateEntity(
//    @PrimaryKey
//    val order_id: Int,
//    val order_status: String,
//    val order_time: String,
//    val estimation_time: String,
//    val total_price: Double,
//    val items: List<OrderItem>
//)
//
////menggabungkan OrderStateEntity dengan OrderItemEntity
//data class OrderWithItems(
//    @Embedded val order: OrderStateEntity,
//    @Relation(
//        parentColumn = "order_id",
//        entityColumn = "order_id"
//    )
//    val items: List<OrderItemEntity>
//)
//
//
//@Entity(tableName = "order_items")
//data class OrderItemEntity(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val order_id: Int, // foreign key secara manual
//    val menu_name: String,
//    val item_details: String,
//    val menu_image: String?, // bisa null
//    val menu_price: Double,
//    val quantity: Int,
//    val add_on: String
//)
