package com.example.eatzy_seller.ui.screen.orderState

//penyesuaian status di database dgn di ui
enum class OrderStatus(val dbValue: String, val displayName: String) {
    SEMUA("", "Semua"),
    KONFIRMASI("waiting", "Konfirmasi"),
    PROSES("processing", "Proses"),
    SELESAI("finished", "Selesai"),
    BATAL("canceled", "Batal");

    companion object {
        fun fromDbValue(value: String): OrderStatus? =
            values().find { it.dbValue == value }

//        fun fromDisplayName(name: String): OrderStatus? =
//            values().find { it.displayName == name }
    }
}