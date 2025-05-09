package com.example.eatzy_seller.ui.screen.orderState

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.eatzy_seller.data.dummyOrders
import com.example.eatzy_seller.data.model.OrderState

class OrderViewModel : ViewModel() {
    var orders by mutableStateOf(dummyOrders)
        private set

    fun updateOrderStatus(orderId: Int, newStatus: String) {
        orders = orders.map {
            if (it.id == orderId) it.copy(status = newStatus) else it
        }
    }

    fun getOrderById(orderId: Int): OrderState? {
        return orders.find { it.id == orderId }
    }
}
