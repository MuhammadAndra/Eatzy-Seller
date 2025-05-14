package com.example.eatzy_seller.ui.screen.orderState

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.eatzy_seller.data.dummyOrders
import com.example.eatzy_seller.data.model.OrderState

class OrderStateViewModel : ViewModel() {

    var orders by mutableStateOf<List<OrderState>>(emptyList())
        private set

    // menyimpan status bar yg dipilih dan menyaring pesanan berdasarkan status
    var selectedStatus by mutableStateOf("Semua")
        private set

    var isLoading by mutableStateOf(false)
        private set

//    var errorMessage by mutableStateOf<String?>(null)
//    pri

    // ubah state pesanan berdasarkan id pesanan
    fun updateOrderStatus(orderId: Int, newStatus: String) {
        orders = orders.map {
            if (it.id == orderId) it.copy(status = newStatus) else it
        }
    }

    // mengambil pesanan berdasarkan id pesanan
    fun getOrderById(orderId: Int): OrderState? {
        return orders.find { it.id == orderId }
    }

    // ubah status filter
    fun updateSelectedStatus(newStatus: String) {
        selectedStatus = newStatus
    }
}
