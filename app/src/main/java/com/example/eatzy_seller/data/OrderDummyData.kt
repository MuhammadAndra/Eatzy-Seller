package com.example.eatzy_seller.data

import com.example.eatzy_seller.data.model.AddOn
import com.example.eatzy_seller.data.model.OrderItem
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.ui.screen.orderState.OrderStatus

val dummyOrders = listOf(
    OrderState(
        orderId = 1,
        orderStatus = OrderStatus.KONFIRMASI.dbValue,
        orderTime = "13/03/2025",
        scheduleTime = "13/03/2025",
        totalPrice = 12000.0,
        items = listOf(
            OrderItem(
                menuId = 1,
                menuName = "Ayam Goreng",
                itemDetails = "nasinya banyakin",
                menuImage = "https://img.freepik.com/premium-photo/ayam-goreng-serundeng-fried-chicken-sprinkled-with-grated-coconut-with-curry-spices-serundeng_431906-4528.jpg?w=1480",
                menuPrice = 10000.0,
                addOns = listOf(
                    AddOn(id = 1, name = "Pedas"),
                    AddOn(id = 2, name = "Tanpa sambal"),
                    AddOn(id = 3, name = "Makan di tempat"),
                    AddOn(id = 4, name = "Tambah tempe"),
                    AddOn(id = 5, name = "Tambah tahu"),
                    AddOn(id = 6, name = "Tambah telur"),
                )
            )
        )
    )
)
