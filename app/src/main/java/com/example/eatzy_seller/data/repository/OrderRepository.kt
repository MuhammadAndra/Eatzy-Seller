package com.example.eatzy_seller.data.repository

import android.util.Log
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.data.model.UpdateOrderStatusRequest
import com.example.eatzy_seller.data.network.api.OrderApiService

class OrderRepository(
    private val api: OrderApiService
//    private val dao: OrderDao
) {

    suspend fun getOrders(token: String, canteenId: Int, status: String): List<OrderState>? {
        return try {
            val response = api.getOrders("Bearer $token", canteenId, status)
            Log.d("OrderRepository", "getOrders response: code=${response.code()}, message=${response.message()}")
            if (response.isSuccessful) response.body() else null // bisa ditambahkan log error
        } catch (e:Exception) {
            Log.e("OrderRepository", "getOrders failed: ${e.message}")
            null
        }
    }

    suspend fun updateOrderStatus(
        token: String,
        orderId: Int,
        statusRequest: UpdateOrderStatusRequest
    ): Boolean {
        return try {
            val response = api.updateOrderStatus(token, orderId, statusRequest)
            if (!response.isSuccessful) {
                Log.e("OrderRepository", "updateOrderStatus failed: ${response.code()} ${response.message()}")
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


