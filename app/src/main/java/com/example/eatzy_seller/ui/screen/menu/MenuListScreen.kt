package com.example.eatzy_seller.ui.screen.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.eatzy_seller.ui.components.BottomNavBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.eatzy_seller.navigation.navGraph.Home
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuListScreen(
    navController: NavController = rememberNavController(),
) {
    val vm: MenuViewModel = viewModel()

    LaunchedEffect(Unit) {
        vm.fetchMenus()
    }

    val menuCategories = vm.menuCategories.collectAsState().value

    Scaffold(
        topBar = { TopBar(title = "Daftar Menu", navController = navController) },
        bottomBar = {
            Column {
                TambahMenuButton {
                    // aksi tambah menu
                }
                Spacer(modifier = Modifier.height(10.dp))
                BottomNavBar(navController = navController)
            }
        }
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
fun TambahMenuButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE89D3C)), // Warna oranye
            shape = MaterialTheme.shapes.large.copy(all = CornerSize(50)), // Membulat penuh
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(56.dp)
        ) {
            Text(
                text = "Tambah Menu",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItem(
    title: String,
    price: Double,
    imageRes: String,
    visibleMenu: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 10.dp),
        shape = MaterialTheme.shapes.medium.copy(CornerSize(8.dp)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            GlideImage(
                model = imageRes,
                contentDescription = "Menu Image",
                transition = CrossFade,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color(0xFFF1F1FA)),
                contentScale = ContentScale.Crop,
                loading = placeholder(ColorPainter(Color.LightGray)),
                failure = placeholder(ColorPainter(Color.Red))
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.Top
            ) {
                // Teks judul dan harga
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Rp${price.toInt()}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    //modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(
                    imageVector =
                        if (visibleMenu) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                    contentDescription = "Visibility",
                    tint = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { /* handle visibility */ }
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color(0xFF3F5185),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { /* handle edit */ }
                )
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { /* handle delete */ }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(2.dp))

}
