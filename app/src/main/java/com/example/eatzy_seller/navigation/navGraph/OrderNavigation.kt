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
import com.example.eatzy_seller.ui.screen.orderState.OrderStateScreen
import com.example.eatzy_seller.ui.screen.orderState.OrderStateViewModel
import kotlinx.serialization.Serializable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.example.eatzy_seller.data.local.AppDatabase
import com.example.eatzy_seller.data.network.RetrofitClient
import com.example.eatzy_seller.data.network.api.OrderApiService
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
) {
    composable(route = Order.route) {
        viewModel.setAuth(token)
        //viewModel.selectedStatus: flow / stateflow untuk menyimpan status pesanan yg sdg dipilih
        //collectAsState(): untuk konversi flow / state menjadi state compose yg bisa di-observe secara reaktif oleh compose UI
        val selectedStatus by viewModel.selectedStatus.collectAsState(initial = OrderStatus.SEMUA) //status awal apk berjalan: semua
//        val orders by viewModel.orders.collectAsStateWithLifecycle()

        LaunchedEffect(Unit){
//        viewModel.setAuth(token)
        viewModel.fetchOrders()
//            Log.d("TEST ORDERS",orders.toString())
        }

        OrderStateScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
}

fun NavGraphBuilder.orderDetailGraph(
    navController: NavHostController,
    token: String,
    viewModel: OrderStateViewModel,
) {
    composable(
        route = "orderDetail/{orderId}",
        arguments = listOf(navArgument("orderId") { type = NavType.IntType })
    ) { backStackEntry ->

        val orderId = backStackEntry.arguments?.getInt("orderId")
        //jika orderId null akan kembali ke screen sebelumnya
        if (orderId == null) {
            navController.popBackStack()
            return@composable
        }

        //mengambil detail pesanan berdasarkan Id
        val selectedOrderState = viewModel.getOrderById(orderId)
        if (selectedOrderState == null) {
            navController.popBackStack()
            return@composable
        }

        //konversi dari orderState ke orderList untuk tampilan detail pesanan
        val order = OrderList(
            orderId = selectedOrderState.orderId,
            orderTime = selectedOrderState.orderTime,
            items = selectedOrderState.items,
            totalPrice = selectedOrderState.totalPrice
        )

        //menampilkan orderDetailScreen
        OrderDetailScreen(
            navController = navController, //navigasi
            order = order, //data pesanan yg ditampilkan, ini dari OrderList
//            viewModel = viewModel,
            onNavigateToOrderFinished = {
                viewModel.updateOrderStatus(
                    orderId = order.orderId,
                    newStatus = OrderStatus.SELESAI.dbValue, //ubah status ke selesai
                    onSuccess = {
                        viewModel.updateSelectedStatus(OrderStatus.SELESAI)
                        navController.navigate(Order.route) {
                            popUpTo(Order.route) { inclusive = true } //kembali ke layar daftar pesanan (OrderStateScreen)
                        }
                    },
                    onError = { Log.e("ORDER_ERROR", it) }
                )
            }
        )
    }
}