package com.example.eatzy_seller.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

//ini untuk tampilan detail pesanan
data class OrderList(
    @SerializedName("order_id")
    val orderId: Int,
    @SerializedName("order_time")
    val orderTime: String,
    @SerializedName("total_price")
    val totalPrice: Double,
    val items: List<OrderItem>
)
//ini untuk di luaran pesanan (dlm card)
data class OrderState(
    @SerializedName("order_id")
    val orderId: Int,
    @SerializedName("order_status")
    val orderStatus: String,
    @SerializedName("order_time")
    val orderTime: String,
    @SerializedName("schedule_time")
    val scheduleTime: String?,
    @SerializedName("total_price")
    val totalPrice: Double,
    val items: List<OrderItem>
)

//ini untuk isi pesanan (dlm card maupun detail pesanan)
@Serializable
data class OrderItem(
    @SerializedName("menu_id")
    val menuId: Int,
    @SerializedName("menu_name")
    val menuName: String,
    @SerializedName("item_details")
    val itemDetails: String?,
    @SerializedName("menu_image")
    val menuImage: String?,
    @SerializedName("menu_price")
    val menuPrice: Double,
    @SerializedName("add_ons")
    val addOns: List<AddOn>
)

@Serializable
data class AddOn(
    @SerializedName("addon_id")
    val id: Int,
    @SerializedName("addon_name")
    val name: String
)

data class UpdateOrderStatusRequest(
    @SerializedName("order_status")
    val orderStatus: String
)