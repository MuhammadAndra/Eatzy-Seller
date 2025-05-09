package com.example.eatzy_seller.data

import com.example.eatzy_seller.data.model.Order
import com.example.eatzy_seller.data.model.OrderItem
import com.example.eatzy_seller.data.model.OrderState

val dummyOrders = listOf(
    OrderState(
        id = 1,
        date = "13/03/2025",
        status = "Konfirmasi",
        items = listOf(OrderItem("Menu 1", 1, "Pedas", "sambal bawang, makan di tempat", 12000.0))
    ),
    OrderState(
        id = 2,
        date = "13/03/2025",
        status = "Proses",
        items = listOf(
            OrderItem("Menu 1", 1, "Sedang", "sambal tomat, bungkus", 12000.0),
            OrderItem("Menu 2", 1, "Tanpa sambal", "sambal bawang, bungkus", 13000.0)
        )
    ),
    OrderState(
        id = 3,
        date = "13/03/2025",
        status = "Konfirmasi",
        items = listOf(OrderItem("Menu 1", 1, "Tidak pedas", "sambal tomat, makan di tempat", 12000.0))
    ),
    OrderState(
        id = 4,
        date = "13/03/2025",
        status = "Konfirmasi",
        items = listOf(
            OrderItem("Menu 1", 1, "Tanpa garam", "sambal tomat, bungkus", 12000.0),
            OrderItem("Menu 2", 1, "Normal", "sambal bawang, bungkus", 13000.0)
        )
    ),
    OrderState(
        id = 4,
        date = "13/03/2025",
        status = "Konfirmasi",
        items = listOf(
        OrderItem("Menu 1", 1, "Tanpa garam", "sambal tomat, bungkus", 12000.0),
        OrderItem("Menu 2", 1, "Normal", "sambal bawang, bungkus", 13000.0),
            OrderItem("Menu 3", 2, "Sedang", "sambal tomat, makan di tempat", 12000.0)
    )
)
)

val order = Order(
    id = 1,
    date = "13/03/2025",
    total = 25.000,
    items = listOf(
        OrderItem("Menu 1", 1, "sambalnya banyakin", "sambal bawang, makan di tempat", 12000.0),
        OrderItem("Menu 2", 1, "nasinya dikit aja","sambal tomat, bungkus",13000.0),
        OrderItem("Menu 3", 1, "sambalnya banyakin","sambal bawang, bungkus",13000.0)
    )
)