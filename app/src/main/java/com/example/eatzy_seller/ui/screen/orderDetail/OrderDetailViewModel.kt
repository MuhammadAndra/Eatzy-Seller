package com.example.eatzy_seller.ui.screen.orderDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatzy_seller.data.model.OrderList
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.data.model.UpdateOrderStatusRequest
import com.example.eatzy_seller.data.repository.OrderRepository
import com.example.eatzy_seller.ui.screen.orderState.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class OrderDetailViewModel(private val repository: OrderRepository): ViewModel() {

    private val _orders = MutableStateFlow<List<OrderList>>(emptyList())
    val orders: StateFlow<List<OrderList>> = _orders

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedStatus = MutableStateFlow(OrderStatus.SELESAI)
    val selectedStatus: StateFlow<OrderStatus> = _selectedStatus

//    private var repository: OrderRepository? = null

    fun fetchOrders() {
        val currentStatus = _selectedStatus.value

        viewModelScope.launch {
            _isLoading.value = true

            val result = repository?.getOrderss(currentStatus)
            if (result != null) {
                _orders.value = result
                _error.value = null
            } else {
                _error.value = "Gagal mengambil pesanan"
            }

            _isLoading.value = false
        }
    }

    fun finishOrder(order: OrderList) = viewModelScope.launch {
        val success = repository?.updateOrderStatus(
            order.orderId,
            UpdateOrderStatusRequest(orderStatus = "finished")
        )
        Log.d("UPDATE_ORDER", "Response: $success")
        if (success == true) {
            // Ubah status terpilih agar fetchOrders ambil yang SELESAI
            _selectedStatus.value = OrderStatus.SELESAI
            fetchOrders()
        } else {
            _error.value = "Gagal update status pesanan"
            Log.e("UPDATE_ORDER", "Update failed")
        }
    }

}