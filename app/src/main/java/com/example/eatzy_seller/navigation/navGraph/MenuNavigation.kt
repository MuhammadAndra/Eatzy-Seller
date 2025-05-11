package com.example.eatzy_seller.navigation.navGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.eatzy_seller.ui.screen.menu.AddMenuScreen
import com.example.eatzy_seller.ui.screen.menu.AddOnItemScreen
import com.example.eatzy_seller.ui.screen.menu.CategoryScreen
import com.example.eatzy_seller.ui.screen.menu.MenuListScreen
import kotlinx.serialization.Serializable

@Serializable
object Menu

@Serializable
object AddMenu

@Serializable
object EditKategori

fun NavGraphBuilder.menuGraph(navController: NavController) {
    composable<Menu> {
        MenuListScreen(navController = navController)
    }
    composable<AddMenu> {
        AddMenuScreen(navController = navController)
    }
    composable<EditKategori> {
        CategoryScreen(navController = navController)
    }

    composable(
        route = "edit_kategori_addon/{namaKategoriAddOn}/{isSingle}",
        arguments = listOf(
            navArgument("namaKategoriAddOn") { type = NavType.StringType },
            navArgument("isSingle") { type = NavType.BoolType }
        )
    ) { backStackEntry ->
        val namaAddOn = backStackEntry.arguments?.getString("namaKategoriAddOn") ?: ""
        val isSingle = backStackEntry.arguments?.getBoolean("isSingle") ?: false
        AddOnItemScreen(namaAddOn = namaAddOn, isSingleChoice = isSingle, navController = navController)
    }

}


