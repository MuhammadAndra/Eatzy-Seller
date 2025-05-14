package com.example.eatzy_seller.data.model

import androidx.compose.ui.graphics.Color

//ini untuk tampilan detail pesanan
data class OrderList(
    val id: Int,
    val date: String,
    val total: Double,
    val items: List<OrderItem>
)
//ini untuk di order state
data class OrderState(
    val id: Int,
    val date: String,
    val estimation: String,
    val status: String,
    val items: List<OrderItem>
)
//ini untuk isi pesanan (dlm card maupun detail pesanan)
data class OrderItem(
    val name: String,
    val quantity: Int,
    val note: String,
    val addOn: String,
    val price: Double,
    val imageUrl: String
)

data class Status(
    val label: String,
    val color: Color
)