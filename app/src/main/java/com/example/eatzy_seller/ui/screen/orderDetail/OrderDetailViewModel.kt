package com.example.eatzy_seller.ui.screen.orderDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.data.model.UpdateOrderStatusRequest
import com.example.eatzy_seller.data.repository.OrderRepository
import com.example.eatzy_seller.ui.screen.orderState.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel: ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Variabel repository untuk berkomunikasi dgn backend API
    private var repository: OrderRepository? = null

    fun finishOrder(order: OrderState) = viewModelScope.launch {
        val success = repository?.updateOrderStatus(
            order.orderId,
            UpdateOrderStatusRequest(orderStatus = OrderStatus.SELESAI.dbValue)
        ) ?: false
        if (success)  else _error.value = "Gagal update status pesanan"

    }
}