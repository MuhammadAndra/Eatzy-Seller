package com.example.eatzy_seller.ui.screen.orderState

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.example.eatzy_seller.data.local.toModel
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.data.model.UpdateOrderStatusRequest
import com.example.eatzy_seller.data.model.User
//import com.example.eatzy_seller.data.model.UpdateOrderStatusRequest
//import com.example.eatzy_seller.data.model.newStatus
import com.example.eatzy_seller.data.network.RetrofitClient
import com.example.eatzy_seller.data.network.api.OrderApiService
import com.example.eatzy_seller.data.repository.OrderRepository
import com.example.eatzy_seller.token
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
//    private val token: String
) : ViewModel() {

    private val _orders = MutableStateFlow<List<OrderState>>(emptyList())
    val orders: StateFlow<List<OrderState>> = _orders.asStateFlow()

    init {
        fetchOrders()
    }

    private val _selectedStatus = MutableStateFlow(OrderStatus.SEMUA)
    val selectedStatus: StateFlow<OrderStatus> = _selectedStatus.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var currentToken: String? = null
    private var currentCanteenId: Int? = null

    fun setUser(token: String, canteenId: Int) {
        currentToken = token
        currentCanteenId = canteenId
        fetchOrders()
    }

    fun fetchOrders() {
        Log.d("OrderViewModel", "Token used: $token")
        val token = currentToken ?: return
//        Log.d("TokenCheck", "currentToken = $currentToken")
        val canteenId = currentCanteenId ?: return
        val status = _selectedStatus.value.dbValue

        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getOrders(token, canteenId, status)
            if (result != null) {
                _orders.value = result
                _error.value = null
            } else {
                _error.value = "Gagal mengambil pesanan"
            }
            _isLoading.value = false
        }
    }

//    fun loadOrders(token: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            _isLoading.value = true
//            try {
//                // For "SEMUA" status, we pass empty string to get all orders
//                val statusParam = if (_selectedStatus.value == OrderStatus.SEMUA) {
//                    ""
//                } else {
//                    _selectedStatus.value.dbValue
//                }
//
//                val result = repository.getOrders(token, statusParam)
//
//                result.onSuccess { orders ->
//                    _orders.value = orders.map { order ->
//                        // Convert database status to our frontend status
//                        order.copy(
//                            order_status = OrderStatus.fromDbValue(order.order_status)?.displayName
//                                ?: order.order_status
//                        )
//                    }
//                }.onFailure {
//                    Log.e("OrderViewModel", "Failed to load orders", it)
//                }
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    //mengubah status filter
    fun updateSelectedStatus(status: OrderStatus, token: String) {
        _selectedStatus.value = status
        fetchOrders()
    }

//    // Fungsi untuk menerima pesanan (pindah ke state "proses")
//    fun acceptOrder(token: String, orderId: Int, canteenId: Int) {
//        updateOrderStatus(token, orderId, OrderStatus.PROSES.dbValue, canteenId, onSuccess = )
//    }
//
//    // Fungsi untuk menolak pesanan (pindah ke state "batal")
//    fun rejectOrder(token: String, orderId: Int, canteenId: Int) {
//        updateOrderStatus(token, orderId, OrderStatus.BATAL.dbValue, canteenId)
//    }
//
//    // Fungsi untuk menyelesaikan pesanan (pindah ke state "selesai"
//    fun completeOrder(token: String, orderId: Int, canteenId: Int) {
//        updateOrderStatus(token, orderId, OrderStatus.SELESAI.dbValue, canteenId)
//    }

    fun updateOrderStatus(
        token: String,
        orderId: Int,
        newStatus: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            val statusEnum = OrderStatus.values().find { it.dbValue == newStatus }
            if (statusEnum == null) {
                onError("Status tidak valid")
                _isLoading.value = false
                return@launch
            }

            val success = repository.updateOrderStatus(
                token,
                orderId,
                UpdateOrderStatusRequest(orderId, statusEnum.dbValue)
            )
            if (success) {
                fetchOrders()
                onSuccess()
            } else {
                onError("Gagal mengubah status pesanan")
            }
            _isLoading.value = false
        }
    }

//    fun acceptOrder(order: OrderState) {
//        viewModelScope.launch {
//            val statusRequest = UpdateOrderStatusRequest(order.order_id, OrderStatus.PROSES.dbValue)
//            val result = repository.updateOrderStatus(token, order.order_id, statusRequest)
//            if (result) {
//                fetchOrders()
//            } else {
//                Log.e("OrderStateViewModel", "Failed to accept order ${order.order_id}")
//            }
//        }
//    }
//
//    fun rejectOrder(order: OrderState) {
//        viewModelScope.launch {
//            val statusRequest = UpdateOrderStatusRequest(order.order_id, OrderStatus.BATAL.dbValue)
//            val result = repository.updateOrderStatus(token, order.order_id, statusRequest)
//            if (result) {
//                fetchOrders()
//            } else {
//                Log.e("OrderStateViewModel", "Failed to reject order ${order.order_id}")
//            }
//        }
//    }
//
//    fun completeOrder(order: OrderState) {
//        viewModelScope.launch {
//            val statusRequest = UpdateOrderStatusRequest(order.order_id, OrderStatus.SELESAI.dbValue)
//            val result = repository.updateOrderStatus(token, order.order_id, statusRequest)
//            if (result) {
//                fetchOrders()
//            } else {
//                Log.e("OrderStateViewModel", "Failed to complete order ${order.order_id}")
//            }
//        }
//    }

//    fun updateStatus(
//        token: String,
//        orderId: Int,
//        newStatus: String,
//        canteenId: Int
//    ) {
//        viewModelScope.launch {
//            val statusRequest = UpdateOrderStatusRequest(order_id = orderId, order_status = newStatus)
//            val success = repository.updateOrderStatus(token, orderId, statusRequest)
//            if (success) {
//                //untuk perbarui daftar pesanan setelah update
//                _orders.value = _orders.value.map {
//                    if (it.order_id == orderId) it.copy(order_status = newStatus) else it
//                }
//            } else {
//                _error.value = "Gagal memperbarui status pesanan"
//            }
//        }
//    }

    fun changeOrderStatus(orderId: Int, newStatus: OrderStatus, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val token = currentToken ?: return onError("Token tidak tersedia")
        val canteenId = currentCanteenId ?: return onError("Canteen ID tidak tersedia")

        updateOrderStatus(token, orderId, newStatus.dbValue, onSuccess, onError)
    }

    fun getOrderById(orderId: Int): OrderState? {
        return _orders.value.find { it.order_id == orderId }
    }
}
//class OrderStateViewModel(
//    private val repository: OrderRepository
//) : ViewModel() {
//
//    private val _orders = MutableStateFlow<List<OrderState>>(emptyList())
//    val orders: StateFlow<List<OrderState>> = _orders.asStateFlow()
//
//    private val _selectedStatus = MutableStateFlow("Semua")
//    val selectedStatus: StateFlow<String> = _selectedStatus.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//
//    private fun mapDisplayNameToDbValue(displayName: String): String {
//        return OrderStatusUI.fromDisplayName(displayName)?.dbValue ?: ""
//    }
//
//    fun loadOrders(token: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            _isLoading.value = true
//            val statusQuery = mapDisplayNameToDbValue(_selectedStatus.value)
//            val result = repository.getOrders(token, statusQuery)
//
//            result.onSuccess { entities ->
//                _orders.value = entities.map { it.toModel() }
//            }.onFailure {
//                Log.e("OrderViewModel", "Failed to load orders: ${it.message}")
//            }
//
//            _isLoading.value = false
//        }
//    }
//
//    fun updateSelectedStatus(status: String, token: String) {
//        _selectedStatus.value = status
//        loadOrders(token)
//    }
//
//    fun updateOrderStatus(
//        token: String,
//        orderId: Int,
//        newStatus: String,
//        onSuccess: () -> Unit = {},
//        onError: (String) -> Unit = {}
//    ) {
//        viewModelScope.launch {
//            val newStatusDbValue = mapDisplayNameToDbValue(newStatus)
//            if (newStatusDbValue.isEmpty()) {
//                onError("Status tidak valid")
//                return@launch
//            }
//
//            val result = repository.updateOrderStatus(token, orderId, newStatusDbValue)
//            result.onSuccess {
//                loadOrders(token)
//                onSuccess()
//            }.onFailure {
//                onError(it.message ?: "Unknown error")
//            }
//        }
//    }
//
//    fun getOrderById(orderId: Int): OrderState? {
//        return _orders.value.find { it.order_id == orderId }
//    }
//}

//class OrderStateViewModel(
//    private val repository: OrderRepository
//) : ViewModel() {
//
//    //    private val orderApi = RetrofitClient.orderApi
//    private val _orders = MutableStateFlow<List<OrderState>>(emptyList())
//
//    //    val orders: StateFlow<List<OrderState>> = _orders.asStateFlow()
//    val orders: StateFlow<List<OrderState>> = _orders.asStateFlow()
//
//
//    private val _selectedStatus = MutableStateFlow("Semua")
//    val selectedStatus: StateFlow<String> = _selectedStatus.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading = _isLoading.asStateFlow()
//
//    // Fungsi helper untuk mapping displayName ke dbValue
//    private fun mapDisplayNameToDbValue(displayName: String): String {
//        return OrderStatusUI.fromDisplayName(displayName)?.dbValue ?: ""
//    }
//
//    fun loadOrders(token: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            _isLoading.value = true
//
//            val statusQuery = mapDisplayNameToDbValue(_selectedStatus.value)
//
////            val result = repository.getOrders(token, _selectedStatus.value)
//            val result = repository.getOrders(token, statusQuery)
//            result.onSuccess { entities ->
//                Log.d("OrderStateViewModel", "Raw entities: $entities")
//                val models = entities.map { it.toModel() }
//                _orders.value = models
//                Log.d(
//                    "OrderStateViewModel",
//                    "Loaded ${models.size} orders with status: $statusQuery"
//                )
//            }.onFailure {
//                Log.e("OrderStateViewModel", "Failed to load orders: ${it.message}")
//            }
//
////            result.onSuccess { entities ->
////                _orders.value = entities.map { it.toModel() }
////            }.onFailure {
////
////            }
//            _isLoading.value = false
//        }
//    }
//
//    fun getOrderById(orderId: Int): OrderState? {
//        return _orders.value.find { it.order_id == orderId }
//    }
//
//    //mengganti status dan reload data
//    fun updateSelectedStatus(status: String, token: String) {
//        _selectedStatus.value = status
//        loadOrders(token)
//    }
//
//    //kirim update status ke API dan refresh daftar pesanan
//    fun updateOrderStatus(
//        token: String,
//        orderId: Int,
//        newStatus: String,  // input UI displayName misal "Proses"
//        onSuccess: () -> Unit = {},
//        onError: (String) -> Unit = {}
//    ) {
//        viewModelScope.launch {
//            val newStatusDbValue = mapDisplayNameToDbValue(newStatus)
//            if (newStatusDbValue.isEmpty()) {
//                onError("Status tidak valid")
//                return@launch
//            }
//
//            val result = repository.updateOrderStatus(token, orderId, newStatusDbValue)
//            result.onSuccess { entities ->
//                loadOrders(token)
//                onSuccess()
//            }.onFailure {
//                onError(it.message ?: "Unknown error")
//            }
//        }
//    }

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
