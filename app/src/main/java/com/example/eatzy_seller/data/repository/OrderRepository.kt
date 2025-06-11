package com.example.eatzy_seller.data.repository

import android.util.Log
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

//        return try {
//            val response = api.getOrders(token, status)
//            if (response.isSuccessful) {
//                val orders = response.body() ?: emptyList()
//                val entities = orders.map { it.toEntity() }
//
//                //menyimpan hasil dari API ke database lokal
//                dao.insertOrderItems(entities)
//
//                Result.success(entities)
//            } else {
//                //kalau gagal ambil dari database (Room)
//                val localOrders = dao.getOrdersByStatus(status).first()
//                Result.success(localOrders)
////                Result.failure(Exception("Failed to fetch orders: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            //fallback menggunakan database jika jaringan gagal
//            val localOrders = dao.getOrdersByStatus(status).first()
//
//            Result.success(localOrders)
//        }
//    }

//    suspend fun updateOrderStatus(token: String, orderId: Int, newStatus: String): Result<Unit> {
//        return try {
//            val body = UpdateOrderStatusRequest(order_status = newStatus)
//            val response = api.updateOrderStatus("Bearer $token", orderId, body)
//
//            if (response.isSuccessful) {
//                val localOrders = dao.getOrdersByStatus(newStatus)
//                localOrders.let {
////                    val updatedOrder = it.copy(order_status = newStatus)
//                    dao.updateStatus(orderId, newStatus)
//                }
//                Result.success(Unit)
//            } else {
//                Result.failure(Exception("Failed to update order status: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//}

//    suspend fun fetchOrdersFromApi(token: String, status: String): Result<Unit> {
//        return try {
//            val response = api.getOrders(token, status)
//            if (response.isSuccessful) {
//                val orders = response.body() ?: emptyList()
//                val orderEntities = orders.map { it.toEntity() }
////                dao.insertOrderItems(orderEntities)
//                dao.getItemsForOrder(orderEntities[0].order_id)
//                Result.success(Unit)
//            } else {
//                Result.failure(Exception("Gagal mengambil data: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }

//    suspend fun updateOrderStatus(token: String, orderId: Int, newStatus: String): Result<Unit> {
//        return try {
//            val response = api.updateOrderStatus(token, orderId, UpdateOrderStatusRequest(order_status = newStatus))
//            if (response.isSuccessful) {
//                dao.updateStatus(orderId, newStatus)
//                Result.success(Unit)
//            } else {
//                Result.failure(Exception("Gagal update: ${response.code()}"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }