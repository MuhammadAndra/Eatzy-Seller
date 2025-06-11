package com.example.eatzy_seller.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.eatzy_seller.navigation.navGraph.Home
import com.example.eatzy_seller.navigation.navGraph.Profile
import com.example.eatzy_seller.navigation.navGraph.Menu
import com.example.eatzy_seller.navigation.navGraph.Order
import kotlin.reflect.KClass

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: KClass<out Any>
)

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home, Home::class),
    BottomNavItem("Menu", Icons.Default.List, Menu::class),
    BottomNavItem("Order", Icons.Default.ShoppingCart, Order::class),
    BottomNavItem("Profile", Icons.Default.Person, Profile::class),
)

@Composable
fun BottomNavBar(navController: NavController) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.route.qualifiedName
            NavigationBarItem(
//                containerColor = Color.White,
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route.qualifiedName!!) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

@Preview
@Composable
private fun BottomNavBarPreview() {
    BottomNavBar(navController = rememberNavController())
}