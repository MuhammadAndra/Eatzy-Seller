package com.example.eatzy_seller.data

import com.example.eatzy_seller.data.model.OrderItem
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.ui.screen.orderState.OrderStatus

val dummyOrders = listOf(
    OrderState(
        order_id = 1,
        order_status = OrderStatus.KONFIRMASI.dbValue,
        order_time = "13/03/2025",
        estimation_time = "10",
        total_price = 12000.0,
        items = listOf(OrderItem(1, "Ayam Goreng", "nasinya banyakin", "https://img.freepik.com/premium-photo/ayam-goreng-serundeng-fried-chicken-sprinkled-with-grated-coconut-with-curry-spices-serundeng_431906-4528.jpg?w=1480", 10000.0, 1, "Pedas, tanpa sambal, makan di tempat, tambah tempe, tambah tahu, tambah telur"))
    )
)