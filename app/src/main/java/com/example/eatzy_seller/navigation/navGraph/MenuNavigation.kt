package com.example.eatzy_seller.navigation.navGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.eatzy_seller.ui.screen.menu.AddOnItemScreen
import com.example.eatzy_seller.ui.screen.menu.MenuListScreen
import com.example.eatzy_seller.ui.screen.menu.TambahMenuScreen
import kotlinx.serialization.Serializable

@Serializable
object Menu

@Serializable
object TambahMenu

fun NavGraphBuilder.menuGraph(navController: NavController) {
    composable<Menu> {
        MenuListScreen(navController = navController)
    }
    composable<TambahMenu> {
        TambahMenuScreen(navController = navController)
    }
    composable(
        route = "edit_kategori_addon/{nama}/{isSingle}",
        arguments = listOf(
            navArgument("nama") { type = NavType.StringType },
            navArgument("isSingle") { type = NavType.BoolType }
        )
    ) { backStackEntry ->
        val nama = backStackEntry.arguments?.getString("nama") ?: ""
        val isSingle = backStackEntry.arguments?.getBoolean("isSingle") ?: false
        AddOnItemScreen(namaKategori = nama, isSingleChoice = isSingle, navController = navController)
    }

}


