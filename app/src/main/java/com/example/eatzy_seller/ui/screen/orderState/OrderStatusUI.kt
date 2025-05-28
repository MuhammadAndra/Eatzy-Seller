package com.example.eatzy_seller.ui.screen.orderState

//penyesuaian status di database dgn di ui
enum class OrderStatusUI(val dbValue: String, val displayName: String) {
    KONFIRMASI("waiting", "Konfirmasi"),
    PROSES("processing", "Proses"),
    SELESAI("finished", "Selesai"),
    BATAL("canceled", "Batal");

    companion object {
        fun fromDbValue(value: String): OrderStatusUI? =
            values().find { it.dbValue == value }

        fun fromDisplayName(name: String): OrderStatusUI? =
            values().find { it.displayName == name }
    }
}