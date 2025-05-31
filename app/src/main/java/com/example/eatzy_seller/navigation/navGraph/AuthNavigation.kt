// navigation/navGraph/AuthNavigation.kt
package com.example.eatzy_seller.navigation.navGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.eatzy_seller.ui.screen.login.LoginScreen
import com.example.eatzy_seller.ui.screen.profile.ProfileScreen
import com.example.eatzy_seller.ui.screen.register.RegisterScreen
import com.example.eatzy_seller.ui.screen.welcomingPage.WelcomingPageScreen
import kotlinx.serialization.Serializable

@Serializable
object Welcome

@Serializable
object Login

@Serializable
object Register



fun NavGraphBuilder.authGraph(navController: NavController) {
    composable<Welcome> {
        WelcomingPageScreen(
            onWelcomePageClick = { navController.navigate(Login) }
        )
    }
    composable<Login> {
        LoginScreen(
            onNavigateToRegister = { navController.navigate(Register) },
            onNavigateToProfile = { navController.navigate(Profile) }
        )
    }
    composable<Register> {
        RegisterScreen(
            onNavigateToLogin = { navController.navigate(Login) }
        )
    }
}