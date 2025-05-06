package com.example.eatzy_seller.ui.screen.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.eatzy_seller.data.model.Menu
import com.example.eatzy_seller.data.model.MenuCategory
import com.example.eatzy_seller.ui.components.BottomNavBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.navigation.NavController
import com.example.eatzy_seller.navigation.navGraph.Home


@Composable
fun MenuListScreen(
    navController: NavHostController = rememberNavController(),
    menuCategories: List<MenuCategory>
    ) {
        Scaffold(
            topBar = { TopBar(title = "Daftar Menu", navController = navController) },
            bottomBar = { BottomNavBar(navController = navController) }
        ) { innerPadding ->
            LazyColumn(
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = innerPadding.calculateBottomPadding() + 8.dp
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                menuCategories.forEach { category ->
                    item {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(start = 8.dp, top = 16.dp, bottom = 4.dp)
                        )
                    }

                    items(category.menus) { menu ->
                        MenuItem(
                            title = menu.title,
                            price = menu.price,
                            imageRes = menu.imageRes,
                            visibleMenu = menu.visibleMenu
                        )
                    }
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    navController: NavController,
    showBackButton: Boolean = true
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = {
                    val navigatedBack = navController.popBackStack()
                    if (!navigatedBack) {
                        navController.navigate(Home::class.qualifiedName!!) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
    )
}


@Composable
fun MenuItem(
    title: String,
    price: String,
    imageRes: String,
    visibleMenu: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 10.dp),
        shape = MaterialTheme.shapes.medium.copy(CornerSize(8.dp)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = imageRes,
                contentDescription = "Menu Image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color(0xFFF1F1FA))
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = price,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector =
                        if (visibleMenu) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                    contentDescription = "Visibility",
                    tint = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { /* handle visibility */ }
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color(0xFF3F5185),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { /* handle edit */ }
                )
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { /* handle delete */ }
                )
            }
        }
    }

}
// data dummy
val categories = listOf(
    MenuCategory(
        name = "Pilihan Crispy",
        menus = listOf(
            Menu("Menu 1", "Rp 12.000", "https://images.app.goo.gl/mLnxTzExrjR2Q2ta9", visibleMenu = false),
            Menu("Menu 2", "Rp 13.000", "https://images.app.goo.gl/mLnxTzExrjR2Q2ta9", visibleMenu = true),
            Menu("Menu 3", "Rp 12.000", "https://images.app.goo.gl/mLnxTzExrjR2Q2ta9", visibleMenu = false)
        )
    ),
    MenuCategory(
        name = "Pilihan Non-Crispy",
        menus = listOf(
            Menu("Menu 4", "Rp 12.000", "https://images.app.goo.gl/mLnxTzExrjR2Q2ta9", visibleMenu = false)
        )
    )
)

@Preview(showBackground = true)
@Composable
fun PreviewMenuList() {
    MenuListScreen(menuCategories = categories)
}
