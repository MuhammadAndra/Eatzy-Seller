package com.example.eatzy_seller.navigation.navGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.eatzy_seller.ui.screen.menu.*
import kotlinx.serialization.Serializable

@Serializable
object MenuManagement

@Serializable
object AddMenu

@Serializable
object EditCategory

@Serializable
object AddAddOnCategory

fun NavGraphBuilder.menuGraph(navController: NavController) {
    //main menu screen
    composable<MenuManagement> {
        MenuManagementScreen(navController = navController)
    }
    composable<AddMenu> {
        AddMenuScreen(navController = navController)
    }
    composable<EditCategory> {
        CategoryScreen(navController = navController)
    }
    composable<AddAddOnCategory> {
        AddOnCategoryScreen(navController = navController, mode = AddOnMode.ADD)
    }

    composable(
        route = "add_on_category/{categoryId}",
        arguments = listOf(
            navArgument("categoryId") { type = NavType.IntType } // atau StringType jika ID berupa string
        )
    ) { backStackEntry ->
        val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
        AddOnCategoryScreen(categoryId = categoryId)
    }


}
