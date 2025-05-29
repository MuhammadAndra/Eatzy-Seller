package com.example.eatzy_seller.navigation.navGraph

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.eatzy_seller.data.model.OrderList
import com.example.eatzy_seller.ui.screen.orderDetail.OrderDetailScreen
import com.example.eatzy_seller.ui.screen.orderState.OrderListScreen
import com.example.eatzy_seller.ui.screen.orderState.OrderStateViewModel
import kotlinx.serialization.Serializable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.example.eatzy_seller.data.local.AppDatabase
import com.example.eatzy_seller.data.network.RetrofitClient
import com.example.eatzy_seller.data.repository.OrderRepository
import com.example.eatzy_seller.ui.screen.orderState.OrderStatus

@Serializable
object Order {
    const val route = "com.example.eatzy_seller.navigation.navGraph.Order"
}

fun NavGraphBuilder.orderGraph(
    navController: NavHostController,
    token: String,
    viewModel: OrderStateViewModel
//    canteenId: Int
) {
    composable(route = Order.route) {

        // Panggil loadOrders pertama kali


        //asli
        val selectedStatus by viewModel.selectedStatus.collectAsState(initial = OrderStatus.SEMUA)
        val orders by viewModel.orders.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.fetchOrders()
            Log.d("TEST ORDERS",orders.toString())
        }

        OrderListScreen(
            navController = navController,
            orders = orders,
            selectedStatus = selectedStatus,
            onStatusSelected = { newStatus -> viewModel.updateSelectedStatus(newStatus, token) },
            onOrderAccepted = { acceptedOrder ->
                viewModel.updateOrderStatus(token, acceptedOrder.order_id, OrderStatus.PROSES.dbValue, onSuccess = {}, onError = {})
                viewModel.updateSelectedStatus(OrderStatus.PROSES, token)
            },
            onOrderRejected = { rejectedOrder ->
                viewModel.updateOrderStatus(token, rejectedOrder.order_id, OrderStatus.BATAL.dbValue, onSuccess = {}, onError = {})
                viewModel.updateSelectedStatus(OrderStatus.BATAL, token)
            },
            onOrderDetailed = { selectedOrder ->
                navController.navigate("orderDetail/${selectedOrder.order_id}")
            }
        )
    }
}

fun NavGraphBuilder.orderDetailGraph(
    navController: NavHostController,
    token: String,
    viewModel: OrderStateViewModel,
//    canteenId: Int
) {
    composable(
        route = "orderDetail/{orderId}",
        arguments = listOf(navArgument("orderId") { type = NavType.IntType })
    ) { backStackEntry ->

        val orderId = backStackEntry.arguments?.getInt("orderId")
        if (orderId == null) {
            navController.popBackStack()
            return@composable
        }

        val selectedOrderState = viewModel.getOrderById(orderId)
        if (selectedOrderState == null) {
            navController.popBackStack()
            return@composable
        }

        val order = OrderList(
            order_id = selectedOrderState.order_id,
            order_time = selectedOrderState.order_time,
            items = selectedOrderState.items,
//            canteen_id = canteenId,
            total_price = selectedOrderState.items.sumOf { it.menu_price * it.quantity }
        )

        OrderDetailScreen(
            navController = navController,
            order = order,
            token = token,
//            viewModel = viewModel,
            onNavigateToOrderFinished = {
                viewModel.updateOrderStatus(
                    token = token,
                    orderId = order.order_id,
                    newStatus = OrderStatus.SELESAI.dbValue,
//                    canteenId = canteenId,
                    onSuccess = {
                        viewModel.updateSelectedStatus(OrderStatus.SELESAI, token)
                        navController.navigate(Order.route) {
                            popUpTo(Order.route) { inclusive = true }
                        }
                    },
                    onError = { Log.e("ORDER_ERROR", it) }
                )
            }
        )
    }
}



//class OrderViewModel : ViewModel() {
////
////    // Menyimpan seluruh list pesanan
////    private val _orders = MutableStateFlow(dummyOrders) // Ganti dengan repo jika sudah terhubung
////    val orders: StateFlow<List<OrderState>> = _orders
////
////    // Menyimpan status terpilih
////    private val _selectedStatus = MutableStateFlow("Semua")
////    val selectedStatus: StateFlow<String> = _selectedStatus
////
////    // Update status terpilih (misalnya "Konfirmasi", "Proses", dst)
////    fun selectStatus(status: String) {
////        _selectedStatus.value = status
////    }
////
////    // Menerima pesanan
////    fun acceptOrder(order: OrderState) {
////        updateOrderStatus(order, "Proses")
////    }
////
////    // Menolak pesanan
////    fun rejectOrder(order: OrderState) {
////        updateOrderStatus(order, "Batal")
////    }
////
////    // Selesaikan pesanan
////    fun completeOrder(order: OrderState) {
////        updateOrderStatus(order, "Selesai")
////    }
////
////    // Update status di list
////    private fun updateOrderStatus(order: OrderState, newStatus: String) {
////        viewModelScope.launch {
////            val updatedList = _orders.value.map {
////                if (it.order_id == order.order_id) it.copy(order_status = newStatus)
////                else it
////            }
////            _orders.value = updatedList
////        }
////    }
////
////    // Mendapatkan pesanan terfilter sesuai status
////    val filteredOrders: StateFlow<List<OrderState>> = combine(_orders, _selectedStatus) { orders, status ->
////        if (status == "Semua") orders else orders.filter { it.order_status == status }
////    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
////}
//
//package com.example.eatzy_seller.navigation.navGraph
//
//import androidx.compose.runtime.*
//import androidx.navigation.NavController
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavHostController
//import androidx.navigation.NavType
//import androidx.navigation.compose.composable
//import androidx.navigation.navArgument
////import com.example.eatzy_seller.data.OrderDummyData
////import com.example.eatzy_seller.data.dummyOrders
//import com.example.eatzy_seller.data.model.OrderList
//import com.example.eatzy_seller.ui.screen.orderState.OrderListScreen
//import com.example.eatzy_seller.ui.screen.orderDetail.OrderDetailScreen
//import com.example.eatzy_seller.ui.screen.orderState.OrderStateViewModel
//import kotlinx.serialization.Serializable
//
//
//@Serializable
//object Order {
//    const val route = "com.example.eatzy_seller.navigation.navGraph.Order"
//}
//
//fun NavGraphBuilder.orderGraph(navController: NavHostController, orderViewModel: OrderStateViewModel, token: String) {
//    composable(route = Order.route) {
//
////        val selectedStatus by remember { derivedStateOf { orderViewModel.selectedStatus } }
////        var orders by remember { mutableStateOf(dummyOrders) }
//        val selectedStatus = orderViewModel.selectedStatus.value
//        val orders by orderViewModel.orders.collectAsState()
//
//        OrderListScreen(
//            navController = navController,
//            orders = orders,
//            selectedStatus = selectedStatus,
//            onStatusSelected = { orderViewModel.updateSelectedStatus(it) },
//            onOrderAccepted = { acceptedOrder ->
//                orderViewModel.updateOrderStatus(token, acceptedOrder.order_id, "Proses", onSuccess = {})
//                orderViewModel.updateSelectedStatus("Proses")
//            },
//            onOrderRejected = { rejectedOrder ->
//                orderViewModel.updateOrderStatus(token, rejectedOrder.order_id, "Batal", onSuccess = {})
//                orderViewModel.updateSelectedStatus("Batal")
//            },
//            onOrderDetailed = { selectedOrder ->
//                navController.navigate("orderDetail/${selectedOrder.order_id}")
//            }
//        )
//    }
//}
//
////fun NavGraphBuilder.orderDetailGraph(navController: NavHostController, orderViewModel: OrderStateViewModel, token: String) {
////    composable(
////        route = "orderDetail/{orderId}",
////        arguments = listOf(navArgument("orderId") { type = NavType.IntType })
////    ) { backStackEntry ->
////        val orderId = backStackEntry.arguments?.getInt("orderId")
////        val selectedOrderState = orderId?.let { orderViewModel.getOrderById(it) }
////
////        selectedOrderState?.let { orderState ->
////            val order = OrderList(
////                id = orderState.id,
////                date = orderState.date,
////                items = orderState.items,
////                total = orderState.items.sumOf { it.price * it.quantity }
////            )
////
////            OrderDetailScreen(
////                navController = navController,
////                order = order,
////                onNavigateToOrderFinished = {
////                    orderViewModel.updateOrderStatus(token, order.id, "Selesai")
////                    orderViewModel.updateSelectedStatus("Selesai")
////                    navController.navigate(Order::class.qualifiedName!!) {
////                        popUpTo(Order::class.qualifiedName!!) { inclusive = true }
////                    }
////                }
////            )
////        }
////    }
////}
//
//fun NavGraphBuilder.orderDetailGraph(
//    navController: NavHostController,
//    orderViewModel: OrderStateViewModel,
//    token: String
//) {
//    composable(
//        route = "orderDetail/{orderId}",
//        arguments = listOf(navArgument("orderId") { type = NavType.IntType })
//    ) { backStackEntry ->
//        val orderId = backStackEntry.arguments?.getInt("orderId")
//
//        if (orderId == null) {
//            // fallback kalau orderId null
//            navController.popBackStack()
//            return@composable
//        }
//
//        val selectedOrderState = orderViewModel.getOrderById(orderId)
//
//        if (selectedOrderState == null) {
//            // fallback kalau order tidak ditemukan
//            navController.popBackStack()
//            return@composable
//        }
//
//        val order = OrderList(
//            id = selectedOrderState.id,
//            date = selectedOrderState.date,
//            items = selectedOrderState.items,
//            total = selectedOrderState.items.sumOf { it.menu_price * it.quantity }
//        )
//
//        OrderDetailScreen(
//            navController = navController,
//            order = order,
//            onNavigateToOrderFinished = {
//                orderViewModel.updateOrderStatus(token, order.id, "Selesai")
//                orderViewModel.updateSelectedStatus("Selesai")
//                navController.navigate("order") {
//                    popUpTo("order") { inclusive = true }
//                }
//            }
//        )
//    }
//}
//
//
//
//
