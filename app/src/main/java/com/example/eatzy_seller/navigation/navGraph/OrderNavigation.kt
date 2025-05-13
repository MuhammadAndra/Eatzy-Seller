package com.example.eatzy_seller.navigation.navGraph

import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
//import com.example.eatzy_seller.data.OrderDummyData
import com.example.eatzy_seller.data.dummyOrders
import com.example.eatzy_seller.data.model.OrderList
import com.example.eatzy_seller.ui.screen.orderState.OrderListScreen
import com.example.eatzy_seller.ui.screen.orderDetail.OrderDetailScreen
import com.example.eatzy_seller.ui.screen.orderState.OrderStateViewModel
import kotlinx.serialization.Serializable

@Serializable
object Order {
    const val route = "com.example.eatzy_seller.navigation.navGraph.Order"
}

fun NavGraphBuilder.orderGraph(navController: NavHostController, orderViewModel: OrderStateViewModel) {
    composable(route = Order.route) {

        val selectedStatus by remember { derivedStateOf { orderViewModel.selectedStatus } }
//        var orders by remember { mutableStateOf(dummyOrders) }
        val orders = orderViewModel.orders

        OrderListScreen(
            navController = navController,
            orders = orders,
            selectedStatus = selectedStatus,
            onStatusSelected = { orderViewModel.updateSelectedStatus(it) },
            onOrderAccepted = { acceptedOrder ->
                orderViewModel.updateOrderStatus(acceptedOrder.id, "Proses")
                orderViewModel.updateSelectedStatus("Proses")
            },
            onOrderRejected = { rejectedOrder ->
                orderViewModel.updateOrderStatus(rejectedOrder.id, "Batal")
                orderViewModel.updateSelectedStatus("Batal")
            },
            onOrderDetailed = { selectedOrder ->
                navController.navigate("orderDetail/${selectedOrder.id}")
            }
        )
    }
}

fun NavGraphBuilder.orderDetailGraph(navController: NavHostController, orderViewModel: OrderStateViewModel) {
    composable(
        route = "orderDetail/{orderId}",
        arguments = listOf(navArgument("orderId") { type = NavType.IntType })
    ) { backStackEntry ->
        val orderId = backStackEntry.arguments?.getInt("orderId")
        val selectedOrderState = orderId?.let { orderViewModel.getOrderById(it) }

        selectedOrderState?.let { orderState ->
            val order = OrderList(
                id = orderState.id,
                date = orderState.date,
                items = orderState.items,
                total = orderState.items.sumOf { it.price * it.quantity }
            )

            OrderDetailScreen(
                navController = navController,
                order = order,
                onNavigateToOrderFinished = {
                    orderViewModel.updateOrderStatus(order.id, "Selesai")
                    orderViewModel.updateSelectedStatus("Selesai")
                    navController.navigate(Order::class.qualifiedName!!) {
                        popUpTo(Order::class.qualifiedName!!) { inclusive = true }
                    }
                }
            )
        }
    }
}


