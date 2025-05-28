package com.example.eatzy_seller.data.repository

import com.example.eatzy_seller.data.local.OrderDao
import com.example.eatzy_seller.data.local.OrderItemEntity
import com.example.eatzy_seller.data.local.OrderStateEntity
import com.example.eatzy_seller.data.local.toEntity
import com.example.eatzy_seller.data.model.OrderList
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.data.model.UpdateOrderStatusRequest
import com.example.eatzy_seller.data.network.api.OrderApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class OrderRepository(
    private val api: OrderApiService,
    private val dao: OrderDao
) {

    suspend fun getOrders(token: String, status: String): Result<List<OrderStateEntity>> {
        return try {
            val response = api.getOrders(token, status)
            if (response.isSuccessful) {
                val orders = response.body() ?: emptyList()
                val entities = orders.map { it.toEntity() }

                //menyimpan hasil dari API ke database lokal
                dao.insertOrderItems(entities)

                Result.success(entities)
            } else {
                //kalau gagal ambil dari database (Room)
                val localOrders = dao.getOrdersByStatus(status).first()
                Result.success(localOrders)
//                Result.failure(Exception("Failed to fetch orders: ${response.code()}"))
            }
        } catch (e: Exception) {
            //fallback menggunakan database jika jaringan gagal
            val localOrders = dao.getOrdersByStatus(status).first()

            Result.success(localOrders)
        }
    }

    suspend fun updateOrderStatus(token: String, orderId: Int, newStatus: String): Result<Unit> {
        return try {
            val body = UpdateOrderStatusRequest(order_status = newStatus)
            val response = api.updateOrderStatus("Bearer $token", orderId, body)

            if (response.isSuccessful) {
                val localOrders = dao.getOrdersByStatus(newStatus)
                localOrders.let {
//                    val updatedOrder = it.copy(order_status = newStatus)
                    dao.updateStatus(orderId, newStatus)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update order status: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
    
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


