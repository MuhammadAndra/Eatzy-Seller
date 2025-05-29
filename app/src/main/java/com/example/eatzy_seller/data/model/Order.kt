package com.example.eatzy_seller.data.model

import kotlinx.serialization.Serializable

//ini untuk tampilan detail pesanan
data class OrderList(
    val order_id: Int,
    val order_time: String,
    val total_price: Double,
//    val canteen_id: Int,
    val items: List<OrderItem>
)
//ini untuk di luaran pesanan (dlm card)
data class OrderState(
    val order_id: Int,
    val order_status: String,
    val order_time: String,
    val estimation_time: String,
    val total_price: Double,
    val items: List<OrderItem>
)

//ini untuk isi pesanan (dlm card maupun detail pesanan)
@Serializable
data class OrderItem(
    val order_id: Int,
    val menu_name: String,
    val item_details: String,
    val menu_image: String?,
    val menu_price: Double,
    val quantity: Int,
    val add_on: String = ""
) {
    //untuk menghitung quantity
    companion object {
        fun groupWithQuantity(items: List<OrderItem>): List<OrderItem> {
            return items
                .groupBy { it.menu_name to it.add_on }
                .map { (_, group) ->
                    val first = group.first()
                    first.copy(quantity = group.size)
                }
        }
    }
}

data class UpdateOrderStatusRequest(
    val order_id: Int,
    val order_status: String
)

//data class newStatus(
//    val order_id: Int,
//    val order_status: String
//)


