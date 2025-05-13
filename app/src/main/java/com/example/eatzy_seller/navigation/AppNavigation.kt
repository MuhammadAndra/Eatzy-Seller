package com.example.eatzy_seller.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.eatzy_seller.navigation.navGraph.Order
import com.example.eatzy_seller.navigation.navGraph.authGraph
import com.example.eatzy_seller.navigation.navGraph.homeGraph
import com.example.eatzy_seller.navigation.navGraph.orderGraph
import com.example.eatzy_seller.navigation.navGraph.testGraph
import com.example.eatzy_seller.navigation.navGraph.orderDetailGraph
import com.example.eatzy_seller.ui.screen.orderState.OrderStateViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val orderViewModel: OrderStateViewModel = viewModel()

    NavHost(navController = navController, startDestination = Order.route) {
        authGraph(navController)
        homeGraph(navController)
        testGraph(navController)
        orderGraph(navController, orderViewModel)
        orderDetailGraph(navController, orderViewModel)
    }
}