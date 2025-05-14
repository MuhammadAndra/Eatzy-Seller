package com.example.eatzy_seller.data

import com.example.eatzy_seller.data.model.OrderItem
import com.example.eatzy_seller.data.model.OrderState

val dummyOrders = listOf(
    OrderState(
        id = 1,
        date = "13/03/2025",
        estimation = "10:00",
        status = "Konfirmasi",
        items = listOf(OrderItem("Ayam Goreng", 1, "Pedas, tanpa sambal, makan di tempat, tambah tempe, tambah tahu, tambah telur", "sambal bawang, makan di tempat", 12000.0, "https://img.freepik.com/premium-photo/ayam-goreng-serundeng-fried-chicken-sprinkled-with-grated-coconut-with-curry-spices-serundeng_431906-4528.jpg?w=1480"))
    ),
    OrderState(
        id = 2,
        date = "13/03/2025",
        estimation = "11:00",
        status = "Proses",
        items = listOf(
            OrderItem("Jamur Crispy", 1, "Sedang", "sambal tomat, bungkus", 12000.0, "https://lh3.googleusercontent.com/-1tk-T-5FB6I/W-zojV95WHI/AAAAAAAAACQ/VPjFLsTS4cIZ61q_IhQt_n0i7H_Nb9xtwCHMYCw/s1600/20181113_094220.png"),
            OrderItem("Ayam Ungkep", 1, "Tanpa sambal", "sambal bawang, bungkus", 13000.0, "https://whattocooktoday.com/wp-content/uploads/2020/03/ayam-ungkep-7-585x878.jpg")
        )
    ),
    OrderState(
        id = 3,
        date = "13/03/2025",
        estimation = "12:00",
        status = "Konfirmasi",
        items = listOf(OrderItem("Menu 1", 1, "Tidak pedas", "sambal tomat, makan di tempat", 12000.0, ""))
    ),
    OrderState(
        id = 4,
        date = "13/03/2025",
        estimation = "13:50",
        status = "Konfirmasi",
        items = listOf(
            OrderItem("Menu 1", 1, "Tanpa garam", "sambal tomat, bungkus", 12000.0, ""),
            OrderItem("Menu 2", 1, "Normal", "sambal bawang, bungkus", 13000.0, "")
        )
    ),
    OrderState(
        id = 5,
        date = "13/03/2025",
        estimation = "14:00",
        status = "Konfirmasi",
        items = listOf(
        OrderItem("Menu 1", 1, "Tanpa garam", "sambal tomat, bungkus", 12000.0, ""),
        OrderItem("Menu 2", 1, "Normal", "sambal bawang, bungkus", 13000.0, ""),
            OrderItem("Menu 3", 2, "Sedang", "sambal tomat, makan di tempat", 12000.0, "")
    )
)
)