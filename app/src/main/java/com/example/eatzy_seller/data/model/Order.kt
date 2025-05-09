package com.example.eatzy_seller.data.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
object OrderList

data class Order(
    val id: Int,
    val date: String,
    val total: Double,
    val items: List<OrderItem>
)

data class OrderState(
    val id: Int,
    val date: String,
    val status: String,
    val items: List<OrderItem>
)
data class OrderItem(
    val name: String,
    val quantity: Int,
    val note: String,
    val addOn: String,
    val price: Double
)

data class Status(
    val label: String,
    val color: Color
)