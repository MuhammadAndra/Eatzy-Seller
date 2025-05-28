package com.example.eatzy_seller.ui.screen.orderState

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatzy_seller.data.local.toModel
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.data.model.UpdateOrderStatusRequest
//import com.example.eatzy_seller.data.model.newStatus
import com.example.eatzy_seller.data.network.RetrofitClient
import com.example.eatzy_seller.data.network.api.OrderApiService
import com.example.eatzy_seller.data.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import kotlin.collections.emptyList

class OrderStateViewModel(
    private val repository: OrderRepository
) : ViewModel() {

    //    private val orderApi = RetrofitClient.orderApi
    private val _orders = MutableStateFlow<List<OrderState>>(emptyList())

    //    val orders: StateFlow<List<OrderState>> = _orders.asStateFlow()
    val orders: StateFlow<List<OrderState>> = _orders


    private val _selectedStatus = MutableStateFlow("Semua")
    val selectedStatus: StateFlow<String> = _selectedStatus.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Fungsi helper untuk mapping displayName ke dbValue
    private fun mapDisplayNameToDbValue(displayName: String): String {
        return OrderStatusUI.fromDisplayName(displayName)?.dbValue ?: ""
    }

    fun loadOrders(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            val statusQuery = mapDisplayNameToDbValue(_selectedStatus.value)

//            val result = repository.getOrders(token, _selectedStatus.value)
            val result = repository.getOrders(token, statusQuery)
            result.onSuccess { entities ->
                Log.d("OrderStateViewModel", "Raw entities: $entities")
                val models = entities.map { it.toModel() }
                _orders.value = models
                Log.d(
                    "OrderStateViewModel",
                    "Loaded ${models.size} orders with status: $statusQuery"
                )
            }.onFailure {
                Log.e("OrderStateViewModel", "Failed to load orders: ${it.message}")
            }

//            result.onSuccess { entities ->
//                _orders.value = entities.map { it.toModel() }
//            }.onFailure {
//
//            }
            _isLoading.value = false
        }
    }

    fun getOrderById(orderId: Int): OrderState? {
        return _orders.value.find { it.order_id == orderId }
    }

    //mengganti status dan reload data
    fun updateSelectedStatus(status: String, token: String) {
        _selectedStatus.value = status
        loadOrders(token)
    }

    //kirim update status ke API dan refresh daftar pesanan
    fun updateOrderStatus(
        token: String,
        orderId: Int,
        newStatus: String,  // input UI displayName misal "Proses"
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val newStatusDbValue = mapDisplayNameToDbValue(newStatus)
            if (newStatusDbValue.isEmpty()) {
                onError("Status tidak valid")
                return@launch
            }

            val result = repository.updateOrderStatus(token, orderId, newStatusDbValue)
            result.onSuccess { entities ->
                loadOrders(token)
                onSuccess()
            }.onFailure {
                onError(it.message ?: "Unknown error")
            }
        }
    }

//    fun fetchOrders(token: String) {
//        viewModelScope.launch {
//            try {
//                val response = orderApi.getOrders(token, status = _selectedStatus.value)
//                if (response.isSuccessful) {
//                    val data = response.body() ?: emptyList()
//                    _orders.value = data
//                    Log.d("OrderFetch", "Orders: $data")
//                } else {
//                    Log.e("OrderFetch", "Error: ${response.code()} - ${response.message()}")
//                }
//            } catch (e: Exception) {
//                Log.e("OrderFetch", "Exception: ${e.message}")
//            }
//        }
//    }

//    fun fetchOrders() {
//        viewModelScope.launch {
//            repository.getOrders(token, _selectedStatus.value).collectLatest { result ->
//                _orders.value = result
//                Log.d("OrderFetch", "Fetched ${result.size} orders.")
//            }
//        }
//    }

//    fun updateOrderStatus(
////        token: String,
//        orderId: Int,
//        newStatus: String,
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit
//    ) {
////        viewModelScope.launch {
////            try {
////                // Pastikan token sudah berformat lengkap "Bearer ..."
////                val body = UpdateOrderStatusRequest( order_status = newStatus)
////                val response = orderApi.updateOrderStatus(token, body)
////                if (response.isSuccessful) {
////                    // Setelah update berhasil, refresh data pesanan
////                    fetchOrders(token)
////                    onSuccess()
////                } else {
////                    Log.e("UpdateOrder", "Failed: ${response.code()} - ${response.message()}")
////                }
////            } catch (e: IOException) {
////                Log.e("UpdateOrder", "Network error: ${e.message}")
////            } catch (e: HttpException) {
////                Log.e("UpdateOrder", "HTTP error: ${e.message}")
////            }
////        }
//        viewModelScope.launch {
//            val result = repository.updateOrderStatus(token, orderId, newStatus)
//            result.onSuccess {
//                fetchOrders()
//                onSuccess()
//            }.onFailure { e ->
//                onError(e.message ?: "Unknown error")
//                Log.e("UpdateOrder", "Failed: ${e.message}")
//            }
//        }
//    }

//    fun updateSelectedStatus(newStatus: String) {
//        _selectedStatus.value = newStatus
//        fetchOrders() // Refresh data pesanan ketika status dipilih
//    }

//    fun getOrderById(orderId: Int): OrderState? {
//        // Sesuaikan kalau field di OrderState berbeda
//        return orders.value.find { it.order_id == orderId }
//    }
}

//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.eatzy_seller.data.local.OrderDao
//import com.example.eatzy_seller.data.local.toEntity
//import com.example.eatzy_seller.data.model.OrderState
//import com.example.eatzy_seller.data.model.UpdateOrderStatusRequest
//import com.example.eatzy_seller.data.network.RetrofitClient
//import com.example.eatzy_seller.data.repository.OrderRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import retrofit2.HttpException
//import java.io.IOException
//
//class OrderStateViewModel(
//    private val repository: OrderRepository
//) : ViewModel() {
//
//    val selectedStatus = MutableStateFlow("Semua")
//
//    val orders = repository.getLocalOrders() // Flow dari Room
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading
//
//    private val _errorMessage = MutableStateFlow<String?>(null)
//    val errorMessage: StateFlow<String?> = _errorMessage
//
//    val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiZW1haWwiOiJhbmRyYWxtYUBleGFtcGxlLmNvbSIsInJvbGUiOiJjYW50ZWVuIiwiaWF0IjoxNzQ3MTkxOTA2LCJleHAiOjE3NDcxOTU1MDZ9.uPqT6DXBYM6JQEhLQ12Pbaf9SxxKxxPNYpu6KKypZMA"
//
//    init {
//        fetchOrders(token)
//    }
//
//    fun fetchOrders(token: String) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _errorMessage.value = null
//            val result = repository.fetchOrdersFromApi(token)
//            _isLoading.value = false
//            result.onFailure {
//                _errorMessage.value = it.message
//            }
//        }
//    }
//
//    fun updateOrderStatus(token: String, orderId: Int, newStatus: String, onSuccess: () -> Unit = {}) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _errorMessage.value = null
//            val result = repository.updateOrderStatus(token, orderId, newStatus)
//            _isLoading.value = false
//            if (result.isSuccess) {
//                onSuccess()
//            } else {
//                _errorMessage.value = result.exceptionOrNull()?.message ?: "Gagal update status"
//            }
//        }
//    }
//
//    fun updateSelectedStatus(newStatus: String) {
//        selectedStatus.value = newStatus
//    }
//}

//class OrderStateViewModel(private val repository: OrderRepository) : ViewModel() {
//
//    private val orderApi = RetrofitClient.orderApi
//
//    // Status yang dipilih filter (default "Semua")
//    val selectedStatus = MutableStateFlow("Semua")
//
//    // List order dari server
//    private val _orders = MutableStateFlow<List<OrderState>>(emptyList())
//    val orders: StateFlow<List<OrderState>> = _orders
//
//    // Status loading (opsional, untuk UI loading indicator)
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading
//
//    // Pesan error (opsional, untuk UI error)
//    private val _errorMessage = MutableStateFlow<String?>(null)
//    val errorMessage: StateFlow<String?> = _errorMessage
//
//    val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiZW1haWwiOiJhbmRyYWxtYUBleGFtcGxlLmNvbSIsInJvbGUiOiJjYW50ZWVuIiwiaWF0IjoxNzQ3MTkxOTA2LCJleHAiOjE3NDcxOTU1MDZ9.uPqT6DXBYM6JQEhLQ12Pbaf9SxxKxxPNYpu6KKypZMA"
//
//    init {
//        fetchOrders(token)
//    }
//
//    // Fungsi fetch order dari server
//    fun fetchOrders(token: String) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _errorMessage.value = null
//            try {
//                val response = orderApi.getOrders(token)
//                if (response.isSuccessful) {
//                    val ordersData = response.body() ?: emptyList()
//                    //update ke state
//                    _orders.value = ordersData
//                    //convert ke entity dan simpan ke room
//                    val orderEntities = ordersData.map { it.toEntity() }
//                    val itemEntities = ordersData.flatMap { order ->
//                        order.items.map { it.toEntity(order.order_id) }
//                    }
//                    //simpan ke database
//                    orderDao.insertOrders(orderEntities)
//                    orderDao.insertOrderItems(itemEntities)
//                } else {
//                    _errorMessage.value = "Gagal mengambil data pesanan: ${response.code()}"
//                }
//            } catch (e: IOException) {
//                _errorMessage.value = "Tidak ada koneksi internet."
//            } catch (e: HttpException) {
//                _errorMessage.value = "Terjadi kesalahan pada server."
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    // Fungsi update status order
//    // parameter onSuccess adalah callback jika update berhasil (misal navigate ke layar lain)
//    fun updateOrderStatus(token: String, orderId: Int, newStatus: String, onSuccess: () -> Unit = {}) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _errorMessage.value = null
//            try {
//                val requestBody = UpdateOrderStatusRequest(order_status = newStatus)
//                val response = orderApi.updateOrderStatus(token, orderId, requestBody)
//                if (response.isSuccessful) {
//                    // update list order agar status terbaru muncul tanpa fetch ulang
//                    val updatedOrderList = _orders.value.map {
//                        if (it.order_id == orderId) it.copy(order_status = newStatus) else it
//                    }
//                    _orders.value = updatedOrderList
//                    onSuccess()
//                } else {
//                    _errorMessage.value = "Gagal update status: ${response.code()}"
//                }
//            } catch (e: IOException) {
//                _errorMessage.value = "Tidak ada koneksi internet."
//            } catch (e: HttpException) {
//                _errorMessage.value = "Terjadi kesalahan pada server."
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//}
