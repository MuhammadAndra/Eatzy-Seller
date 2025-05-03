package com.example.eatzy_seller.navigation.navGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.eatzy_seller.ui.screen.canteen.CanteenScreen
import com.example.eatzy_seller.ui.screen.home.HomeScreen
import com.example.eatzy_seller.ui.screen.profile.ProfileScreen

import kotlinx.serialization.Serializable

@Serializable
object Search

@Serializable
object Canteen

@Serializable
object Home

@Serializable
object Profile

fun NavGraphBuilder.homeGraph(navController: NavController) {
    composable<Search> {

    }
    composable<Canteen> {
        CanteenScreen(onNavigateToLogin = { navController.navigate(Login) })
    }

    composable<Home> {
        HomeScreen(navController = navController)
    }

    composable<Profile> {
        ProfileScreen(navController = navController)
    }

}