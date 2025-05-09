package com.example.eatzy_seller.navigation.navGraph

import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
//import com.example.eatzy_seller.data.OrderDummyData
import com.example.eatzy_seller.data.dummyOrders
import com.example.eatzy_seller.data.model.Order
import com.example.eatzy_seller.data.model.OrderItem
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.data.order
import com.example.eatzy_seller.ui.screen.login.LoginScreen
import com.example.eatzy_seller.ui.screen.orderState.OrderListScreen
import com.example.eatzy_seller.ui.screen.orderDetail.OrderDetailScreen
import com.example.eatzy_seller.ui.screen.orderState.OrderViewModel

//import kotlinx.serialization.Serializable


//@Serializable
//object Order

fun NavGraphBuilder.orderGraph(navController: NavController, orderViewModel: OrderViewModel) {
    composable(route = "orderList") {
        var selectedStatus by remember { mutableStateOf("Semua") }
        var orders by remember { mutableStateOf(dummyOrders) }

        OrderListScreen(
            orders = orders,
            selectedStatus = selectedStatus,
            onStatusSelected = { selectedStatus = it },
            onOrderAccepted = { acceptedOrder ->
                orders = orders.map {
                    if (it.id == acceptedOrder.id) it.copy(status = "Proses") else it
                }
                selectedStatus = "Proses"
            },
            onOrderRejected = { rejectedOrder ->
                orders = orders.map {
                    if (it.id == rejectedOrder.id) it.copy(status = "Batal") else it
                }
                selectedStatus = "Batal"
            },
            onOrderDetailed = { selectedOrder ->
                navController.navigate("orderDetail/${selectedOrder.id}")
            }
        )
    }
}

fun NavGraphBuilder.orderDetailGraph(navController: NavController, orderViewModel: OrderViewModel) {
    composable(
        route = "orderDetail/{orderId}",
        arguments = listOf(navArgument("orderId") { type = NavType.IntType })
    ) { backStackEntry ->
        val orderId = backStackEntry.arguments?.getInt("orderId")
        val selectedOrderState = dummyOrders.find { it.id == orderId }

        selectedOrderState?.let { orderState ->
            val order = Order(
                id = orderState.id,
                date = orderState.date,
                items = orderState.items,
                total = orderState.items.sumOf { it.price * it.quantity }
            )

            OrderDetailScreen(
                order = order,
                onNavigateToOrderFinished = {
                    orderViewModel.updateOrderStatus(order.id, "Selesai")
                    navController.popBackStack()
                    // Tambahan logika jika ingin update status ke "Selesai"
                }
            )
        }
    }
}


