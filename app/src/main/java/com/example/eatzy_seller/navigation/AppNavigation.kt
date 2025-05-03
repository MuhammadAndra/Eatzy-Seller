package com.example.eatzy_seller.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.eatzy_seller.navigation.navGraph.Home
import com.example.eatzy_seller.navigation.navGraph.Login
import com.example.eatzy_seller.navigation.navGraph.Test
import com.example.eatzy_seller.navigation.navGraph.authGraph
import com.example.eatzy_seller.navigation.navGraph.homeGraph
import com.example.eatzy_seller.navigation.navGraph.menuGraph
import com.example.eatzy_seller.navigation.navGraph.orderGraph
import com.example.eatzy_seller.navigation.navGraph.testGraph

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Home) {
        authGraph(navController)
        homeGraph(navController)
        testGraph(navController)
        orderGraph(navController)
        menuGraph(navController)
    }
}