package com.example.eatzy_seller.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.eatzy_seller.navigation.navGraph.Home
import com.example.eatzy_seller.navigation.navGraph.Login
import com.example.eatzy_seller.navigation.navGraph.Welcome
import com.example.eatzy_seller.navigation.navGraph.Order
import com.example.eatzy_seller.navigation.navGraph.authGraph
import com.example.eatzy_seller.navigation.navGraph.homeGraph
import com.example.eatzy_seller.navigation.navGraph.menuGraph
import com.example.eatzy_seller.navigation.navGraph.orderGraph
//import com.example.eatzy_seller.navigation.navGraph.testGraph
import com.example.eatzy_seller.navigation.navGraph.testGraph
import com.example.eatzy_seller.navigation.navGraph.orderDetailGraph
import com.example.eatzy_seller.token
import com.example.eatzy_seller.ui.screen.orderState.OrderStateViewModel

@Composable
//fun AppNavigation(modifier: Modifier = Modifier) {
fun AppNavigation(viewModel: OrderStateViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Welcome) {
        authGraph(navController)
        homeGraph(navController)
//        testGraph(navController)
        orderGraph(navController)
        menuGraph(navController)
//        testGraph(navController)
        orderGraph(navController, "Bearer $token", viewModel)
        orderDetailGraph(navController, "Bearer $token", viewModel)
    }
}