package com.example.eatzy_seller.data.network.api

import com.example.eatzy_seller.data.model.OrderList
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.data.model.UpdateOrderStatusRequest
//import com.example.eatzy_seller.data.model.OrderState
//import com.example.eatzy_seller.data.model.UpdateOrderStatusRequest
//import com.example.eatzy_seller.data.model.newStatus
import com.example.eatzy_seller.data.network.RetrofitClient
//import com.example.eatzy_seller.navigation.navGraph.Order
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

import retrofit2.http.*

interface OrderApiService {

    //arahin ke endpoint "orders"
    @GET("orders/canteen")
    suspend fun getOrders(
        @Header("Authorization") token: String,
//        @Header("user_id") user_id: Int //mengirimkan userID di url
//        @Query("status") status: String
    ): Response<List<OrderState>> //mengembalikan list dari OrderState

    @PUT("orders/canteen/{order_id}")
    suspend fun updateOrderStatus(
        @Header("Authorization") token: String,
        @Path("order_id") orderId: Int,
        @Body statusRequest: UpdateOrderStatusRequest
    ): Response<Unit>
}