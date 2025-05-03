package com.example.eatzy_seller.navigation.navGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.eatzy_seller.ui.screen.menu.MenuScreen
import kotlinx.serialization.Serializable

@Serializable
object Menu


fun NavGraphBuilder.menuGraph(navController: NavController) {

    composable<Menu> {
        MenuScreen(navController = navController)
    }
}