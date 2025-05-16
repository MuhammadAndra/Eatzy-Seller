package com.example.eatzy_seller.ui.screen.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.eatzy_seller.data.model.Menu
import com.example.eatzy_seller.data.model.MenuCategory
import com.example.eatzy_seller.navigation.navGraph.AddMenu
import com.example.eatzy_seller.ui.components.DeleteMenuDialog
import com.example.eatzy_seller.ui.components.TopBarMenu
import com.example.eatzy_seller.ui.theme.SecondColor
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuListScreen(navController: NavController = rememberNavController()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val vm: MenuViewModel = viewModel()

    LaunchedEffect(Unit) {
        vm.fetchMenus()
    }

    //val menuCategories = vm.menuCategories.collectAsState().value
    val menuCategories = dummyCategories

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                TopBarMenu(title = "Daftar Menu", navController = navController)
                buttonStatus("Menu")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(AddMenu) },
                containerColor = SecondColor,
                shape = CircleShape
            ) {
                Icon(
                    Icons.Default.Add,
                    modifier = Modifier.size(35.dp),
                    contentDescription = "Tambah Kategori",
                    tint = Color.White
                )
            }
        },
        bottomBar = {

            BottomNavBar(navController = navController)

        }
    ) { innerPadding ->
        MenuListContent(
            innerPadding = innerPadding,
            menuCategories = menuCategories,
            onDelete = { vm.deleteMenu(it) },
            onShowSnackbar = { message ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        )
    }

}

@Composable
fun MenuListContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    menuCategories: List<MenuCategory>,
    onDelete: (Int) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = innerPadding.calculateTopPadding() + 8.dp,
            bottom = innerPadding.calculateBottomPadding() + 8.dp
        ),
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        menuCategories.forEach { category ->
            item {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp) // padding dalam border
                ) {
                    Text(
                        text = category.categoryName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 8.dp)
                    )

                    category.menus.forEachIndexed { index, menu ->
                        MenuItem(
                            menu = menu,
                            onDelete = { onDelete(menu.idMenu) },
                            onShowSnackbar = onShowSnackbar
                        )

                        if (index != category.menus.lastIndex) {
                            HorizontalDivider(
                                thickness = 0.5.dp,
                                color = Color(0xFFBDBDBD),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }



    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItem(
    menu: Menu,
    onDelete: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        DeleteMenuDialog(
            objek = "Menu",
            title = menu.namaMenu,
            onConfirmDelete = {
                onDelete()
                onShowSnackbar("Menu \"${menu.namaMenu}\" berhasil dihapus")
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    val formattedPrice = NumberFormat.getNumberInstance(Locale("id", "ID")).format(menu.price)

    Row(
        verticalAlignment = Alignment.Top
    ) {
        GlideImage(
            model = menu.imageRes,
            contentDescription = "Menu Image",
            transition = CrossFade,
            modifier = Modifier
                .size(90.dp)
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
            Text(
                text = menu.namaMenu,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = "Rp$formattedPrice",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,

            ) {
                Checkbox(
                    checked = menu.visibleMenu,
                    onCheckedChange = { /* handle toggle */ }
                )
                Text("Tampilkan Add-On", fontSize = 14.sp)
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(10.dp)
        ) {
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
                tint = Color(0xFFFC2433),
                modifier = Modifier
                    .size(20.dp)
                    .clickable { showDeleteDialog = true }
            )
        }
    }
}

val statuses = listOf("Menu", "Add-On")

@Composable
fun buttonStatus(screen: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        statuses.forEach { status ->
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (status == screen) Color(0xFFF59A2F) else Color.Transparent,
                    contentColor = if (status == screen) Color.White else Color.Black
                ),
                contentPadding = PaddingValues(start = 30.dp, end = 30.dp)
            ) {
                Text(
                    text = status,
                    fontWeight = if (status == screen) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

        }
    }
}


val dummyCategories = listOf(
    MenuCategory(
        idCategory = 1,
        idCanteen = 1,
        categoryName = "Makanan",
        menus = listOf(
            Menu(
                idMenu = 1,
                namaMenu = "Nasi Goreng",
                price = 20000.0,
                imageRes = "https://via.placeholder.com/150",
                visibleMenu = true
            ),
            Menu(
                idMenu = 2,
                namaMenu = "Mie Ayam",
                price = 18000.0,
                imageRes = "https://via.placeholder.com/150",
                visibleMenu = false
            )
        )
    ),
    MenuCategory(
        idCategory = 2,
        idCanteen = 1,
        categoryName = "Minuman",
        menus = listOf(
            Menu(
                idMenu = 3,
                namaMenu = "Es Teh",
                price = 5000.0,
                imageRes = "https://via.placeholder.com/150",
                visibleMenu = true
            )
        )
    )
)

@Preview(showBackground = true)
@Composable
fun PreviewButtonStatus() {
    buttonStatus("Add-On")
}

@Preview(showBackground = true)
@Composable
fun PreviewMenuListScreen() {
    MenuListScreen()
}

@Preview(showBackground = true)
@Composable
fun PreviewMenuListContent() {

    MenuListContent(
        innerPadding = PaddingValues(0.dp),
        menuCategories = dummyCategories,
        onDelete = {},
        onShowSnackbar = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMenuItem() {
    MenuItem(
        menu = Menu(
            idMenu = 1,
            namaMenu = "Nasi Goreng Spesial",
            price = 25000.0,
            imageRes = "https://via.placeholder.com/150",
            visibleMenu = true
        ),
        onDelete = {},
        onShowSnackbar = {}
    )
}

