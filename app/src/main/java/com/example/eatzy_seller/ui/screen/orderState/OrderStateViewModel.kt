package com.example.eatzy_seller.ui.screen.orderState

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.data.model.UpdateOrderStatusRequest
import com.example.eatzy_seller.data.network.RetrofitClient
import com.example.eatzy_seller.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import kotlin.collections.emptyList

class OrderStateViewModel : ViewModel() {

    private val _orders = MutableStateFlow<List<OrderState>>(emptyList())
    val orders: StateFlow<List<OrderState>> = _orders

    private val _selectedStatus = MutableStateFlow(OrderStatus.SEMUA)
    val selectedStatus: StateFlow<OrderStatus> = _selectedStatus

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var repository: OrderRepository? = null

    /**
     * Set token untuk autentikasi.
     * Harus dipanggil sebelum fetchOrders atau updateOrderStatus.
     */
    fun setAuth(token: String) {
        val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
        this.repository = OrderRepository(RetrofitClient.orderApi, bearerToken)
    }

    /**
     * Ambil daftar pesanan berdasarkan status terpilih.
     */
    fun fetchOrders() {
        val currentStatus = _selectedStatus.value

        viewModelScope.launch {
            _isLoading.value = true

            val result = repository?.getOrders(currentStatus)
            if (result != null) {
                _orders.value = result
                _error.value = null
            } else {
                _error.value = "Gagal mengambil pesanan"
            }

            _isLoading.value = false
        }
    }

    fun getOrderById(orderId: Int): OrderState? {
        return _orders.value.find { it.orderId == orderId }
    }

    /**
     * Ubah status yang dipilih dan ambil ulang data.
     */
    //ubah status di state ke state, untuk filter daftar pesanan
    fun updateSelectedStatus(newStatus: OrderStatus) {
        _selectedStatus.value = newStatus
        fetchOrders()
    }

    /**
     * Update status dari suatu pesanan.
     */
    //ubah status, setelah menyelesaikan suatu proses, misal dari konfirmasi ke proses
    fun updateOrderStatus(
        orderId: Int,
        newStatus: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch {
        try {
            val success = repository?.updateOrderStatus(orderId, UpdateOrderStatusRequest(orderStatus = newStatus)) ?: false
            if (success) {
                onSuccess()
            } else {
                onError("Gagal update status pesanan")
            }
        } catch (e: Exception) {
            onError("Terjadi kesalahan: ${e.message}")
        }
    }

    fun acceptOrder(order: OrderState) = viewModelScope.launch {
        val success = repository?.updateOrderStatus(
            order.orderId,
            UpdateOrderStatusRequest(orderStatus = OrderStatus.PROSES.dbValue)
        ) ?: false
        if (success) fetchOrders() else _error.value = "Gagal update status pesanan"
    }

    fun rejectOrder(order: OrderState) = viewModelScope.launch {
        val success = repository?.updateOrderStatus(
            order.orderId,
            UpdateOrderStatusRequest(orderStatus = OrderStatus.BATAL.dbValue)
        ) ?: false
        if (success) fetchOrders() else _error.value = "Gagal update status pesanan"
    }
}