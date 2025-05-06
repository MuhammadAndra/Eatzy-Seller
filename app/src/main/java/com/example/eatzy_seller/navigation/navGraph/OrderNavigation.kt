package com.example.eatzy_seller.navigation.navGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.eatzy_seller.ui.order.OrderScreen
import kotlinx.serialization.Serializable

@Serializable
object Order

fun NavGraphBuilder.orderGraph(navController: NavController) {

    composable<Order> {
        OrderScreen(navController = navController)
    }
}