package com.example.eatzy_seller.ui.screen.menu


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@Composable
fun MenuScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: MenuViewModel = viewModel()
) {
    val menuCategories by viewModel.menuCategories.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchMenus()
    }

    MenuListScreen(
        navController = navController,
        menuCategories = menuCategories
    )
}
