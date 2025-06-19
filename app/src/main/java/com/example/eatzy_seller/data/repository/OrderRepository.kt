package com.example.eatzy_seller.data.repository

import android.util.Log
import com.example.eatzy_seller.data.model.OrderList
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.data.model.UpdateOrderStatusRequest
import com.example.eatzy_seller.data.network.api.OrderApiService
import com.example.eatzy_seller.ui.screen.orderState.OrderStatus

class OrderRepository(
    private val api: OrderApiService,
    private val token: String
//    private val dao: OrderDao
) {
    suspend fun getOrders(status: OrderStatus): List<OrderState>? {
        return try {
            //untuk ambil pesanan dari server berdasarkan status tertentu
            val response = api.getOrders("$token", status = status.dbValue) // pakai enum.value
            if (response.isSuccessful) {
                //kalau sukses return data dari orderState
                Log.d("API_RESPONSE", "Order response: ${response.body()}")
                response.body()
            } else {
                //kalau gagal akan muncul log error dan return null
                Log.e("API_ERROR", "Error: ${response.code()} ${response.message()}")
                Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Exception: ${e.message}")
            null
        }
    }

    suspend fun getOrderss(status: OrderStatus): List<OrderList>? {
        return try {
            //untuk ambil pesanan dari server berdasarkan status tertentu
            val response = api.getOrders("$token", status = status.dbValue) // pakai enum.value
            if (response.isSuccessful) {
                //kalau sukses return data dari orderState
                Log.d("API_RESPONSE", "Order response: ${response.body()}")
                response.body()?.map { it ->
                    OrderList(
                        orderId = it.orderId,
                        orderTime = it.orderTime,
                        items = it.items,
                        totalPrice = it.totalPrice
                    )
                }
            } else {
                //kalau gagal akan muncul log error dan return null
                Log.e("API_ERROR", "Error: ${response.code()} ${response.message()}")
                Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Exception: ${e.message}")
            null
        }
    }

    suspend fun updateOrderStatus(
        orderId: Int,
        statusRequest: UpdateOrderStatusRequest
    ): Boolean {
        return try {
            val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = api.updateOrderStatus("$token", orderId, statusRequest)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                Log.e("OrderRepository", "updateOrderStatus failed: ${response.code()} ${response.message()} $errorBody")
            }
            response.isSuccessful
        } catch (e:Exception) {
            Log.e("OrderRepository", "updateOrderStatus failed: ${e.message}")
            false
        }
    }
}