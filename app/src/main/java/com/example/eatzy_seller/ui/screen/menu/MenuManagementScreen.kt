package com.example.eatzy_seller.ui.screen.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.eatzy_seller.data.model.AddOn
import com.example.eatzy_seller.data.model.AddOnCategory
import com.example.eatzy_seller.data.model.Menu
import com.example.eatzy_seller.data.model.MenuCategory
import com.example.eatzy_seller.data.model.dummyAddOnCategories
import com.example.eatzy_seller.data.model.dummyMenuCategories
import com.example.eatzy_seller.navigation.navGraph.AddAddOnCategory
import com.example.eatzy_seller.navigation.navGraph.AddMenu
import com.example.eatzy_seller.ui.components.*
import com.example.eatzy_seller.ui.theme.SecondColor
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

enum class MenuType {
    REGULAR_MENU, ADD_ON
}

@Composable
fun MenuManagementScreen(navController: NavController = rememberNavController()) {
    //default menu screen
    var currentMenuType by remember { mutableStateOf(MenuType.REGULAR_MENU) }

    when (currentMenuType) {
        MenuType.REGULAR_MENU -> MenuListScreen(
            navController = navController,
            onSwitchToAddOn = { currentMenuType = MenuType.ADD_ON }
        )
        MenuType.ADD_ON -> AddOnListScreen(
            navController = navController,
            onSwitchToMenu = { currentMenuType = MenuType.REGULAR_MENU }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuListScreen(
    navController: NavController = rememberNavController(),
    onSwitchToAddOn: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val vm: MenuViewModel = viewModel()

    LaunchedEffect(Unit) {
        vm.fetchMenus()
    }

    //pakai data dummy dulu
    val menuCategories = dummyMenuCategories

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                TopBarMenu(title = "Daftar Menu", navController = navController, showBackButton = false)
                ButtonStatus(
                    currentScreen = "Menu",
                    onMenuClick = { /* already on menu */ },
                    onAddOnClick = onSwitchToAddOn
                )
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
                    contentDescription = "Tambah Menu",
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
            isAddOn = false,
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
    isAddOn: Boolean,
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
                        .padding(8.dp)
                ) {
                    Text(
                        text = category.categoryName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 8.dp)
                    )

                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = Color(0xFFBDBDBD),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    category.menus.forEachIndexed { index, menu ->
                        MenuItem(
                            menu = menu,
                            isAddOn = isAddOn,
                            onDelete = { onDelete(menu.menuId) },
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
    isAddOn: Boolean,
    onDelete: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        DeleteMenuDialog(
            objek = if (isAddOn) "Add-On" else "Menu",
            title = menu.menuName,
            onConfirmDelete = {
                onDelete()
                onShowSnackbar("${if (isAddOn) "Add-On" else "Menu"} \"${menu.menuName}\" berhasil dihapus")
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    val formattedPrice = NumberFormat.getNumberInstance(Locale("id", "ID")).format(menu.menuPrice)

    Row(verticalAlignment = Alignment.Top) {
        GlideImage(
            model = menu.menuImageRes,
            contentDescription = "Menu Image",
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
                text = menu.menuName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = "Rp$formattedPrice",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = menu.menuAvailable,
                    onCheckedChange = { /* handle toggle */ }
                )
                Text(
                    text = if (isAddOn) "Tampilkan Add-On" else "Tampilkan Menu",
                    fontSize = 14.sp
                )
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


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AddOnListScreen(
    navController: NavController = rememberNavController(),
    onSwitchToMenu: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val vm: MenuViewModel = viewModel()

    LaunchedEffect(Unit) {
        vm.fetchMenus()
    }

    val addOnCategories = dummyAddOnCategories

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                TopBarMenu(title = "Daftar Add-On", navController = navController, showBackButton = false)
                //pindah button menu and add-on
                ButtonStatus(
                    currentScreen = "Add-On",
                    onMenuClick = onSwitchToMenu,
                    onAddOnClick = { /* already on add-on */ }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(AddAddOnCategory) },
                containerColor = SecondColor,
                shape = CircleShape
            ) {
                Icon(
                    Icons.Default.Add,
                    modifier = Modifier.size(35.dp),
                    contentDescription = "Tambah Add-On",
                    tint = Color.White
                )
            }
        },
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        AddOnListContent(
            innerPadding = innerPadding,
            addOnCategories = addOnCategories,
            onDelete = { /*vm.deleteAddOn(it)*/ },
            onShowSnackbar = { message ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            },
            navController = navController
        )
    }
}
@Composable
fun AddOnListContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    addOnCategories: List<AddOnCategory>,
    onDelete: (Int) -> Unit,
    onShowSnackbar: (String) -> Unit,
    navController: NavController
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = innerPadding.calculateTopPadding() + 8.dp,
            bottom = innerPadding.calculateBottomPadding() + 8.dp
        ),
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        addOnCategories.forEach { category ->
            item {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = category.addOnCategoryName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.weight(1f).padding(start = 8.dp)
                        )
                        Text(
                            text = if (category.addOnCategoryMultiple) "Multiple" else "Single",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = Color(0xFFBDBDBD),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    category.addOns.forEachIndexed { index, addOn ->
                        AddOnItem(
                            addOn = addOn,
                            onDelete = { onDelete(addOn.AddOnId) },
                            onShowSnackbar = onShowSnackbar,
                            navController = navController
                        )

                        if (index != category.addOns.lastIndex) {
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
fun AddOnItem(
    addOn: AddOn,
    onDelete: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    navController: NavController
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        DeleteMenuDialog(
            objek = "Add-On",
            title = addOn.AddOnName,
            onConfirmDelete = {
                onDelete()
                onShowSnackbar("Add-On \"${addOn.AddOnName}\" berhasil dihapus")
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    val formattedPrice = NumberFormat.getNumberInstance(Locale("id", "ID")).format(addOn.AddOnPrice)

    Row(verticalAlignment = Alignment.Top) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = addOn.AddOnName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 2.dp, start = 12.dp)
            )
            Text(
                text = "Rp$formattedPrice",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                modifier = Modifier.padding(start = 12.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = addOn.AddOnAvailable,
                    onCheckedChange = { /* handle toggle */ }
                )
                Text(
                    text = "Tampilkan Add-On",
                    fontSize = 14.sp
                )
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
                    .clickable { navController.navigate("add_on_category//{categoryName}/{isSingleChoice}") }
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


@Composable
fun ButtonStatus(
    currentScreen: String,
    onMenuClick: () -> Unit,
    onAddOnClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color.White) // Ganti dengan warna latar yang diinginkan
            .padding(vertical = 8.dp), // Opsional: Tambahkan padding agar tombol tidak terlalu mepet
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        listOf("Menu", "Add-On").forEach { status ->
            Button(
                onClick = {
                    when (status) {
                        "Menu" -> onMenuClick()
                        "Add-On" -> onAddOnClick()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (status == currentScreen) Color(0xFFF59A2F) else Color.Transparent,
                    contentColor = if (status == currentScreen) Color.White else Color.Black
                ),
                contentPadding = PaddingValues(start = 30.dp, end = 30.dp)
            ) {
                Text(
                    text = status,
                    fontWeight = if (status == currentScreen) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}


// Preview functions for Add-On screens
@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewAddOnListScreen() {
    AddOnListScreen()
}

@Preview(showBackground = true)
@Composable
fun PreviewAddOnListContent() {
    Surface {
        AddOnListContent(
            innerPadding = PaddingValues(0.dp),
            addOnCategories = dummyAddOnCategories,
            onDelete = {},
            onShowSnackbar = {},
            navController = rememberNavController()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddOnItem() {
    Surface {
        Column {
            AddOnItem(
                addOn = AddOn(
                    AddOnId = 1,
                    AddOnName = "Extra Cheese",
                    AddOnPrice = 5000.0,
                    AddOnAvailable = true
                ),
                onDelete = {},
                onShowSnackbar = {},
                navController = rememberNavController()
            )
            AddOnItem(
                addOn = AddOn(
                    AddOnId = 2,
                    AddOnName = "Spicy Level 2",
                    AddOnPrice = 0.0,
                    AddOnAvailable = false
                ),
                onDelete = {},
                onShowSnackbar = {},
                navController = rememberNavController()
            )
        }
    }
}

// Preview functions remain the same...
@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewMenuManagementScreen() {
    MenuManagementScreen()
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonStatus() {
    Column {
        ButtonStatus(
            currentScreen = "Menu",
            onMenuClick = {},
            onAddOnClick = {}
        )
        Spacer(modifier = Modifier.height(16.dp))
        ButtonStatus(
            currentScreen = "Add-On",
            onMenuClick = {},
            onAddOnClick = {}
        )
    }
}

//menu
@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewMenuListScreen() {
    MenuListScreen()
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewMenuListContent() {
    Surface {
        MenuListContent(
            innerPadding = PaddingValues(0.dp),
            menuCategories = dummyMenuCategories,
            isAddOn = false,
            onDelete = {},
            onShowSnackbar = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMenuItem() {
    Surface {
        Column {
            MenuItem(
                menu = Menu(
                    menuId = 1,
                    menuName = "Nasi Goreng Spesial",
                    menuPrice = 25000.0,
                    menuImageRes = "https://via.placeholder.com/150",
                    menuAvailable = true
                ),
                isAddOn = false,
                onDelete = {},
                onShowSnackbar = {}
            )
            MenuItem(
                menu = Menu(
                    menuId = 2,
                    menuName = "Extra Keju",
                    menuPrice = 5000.0,
                    menuImageRes = "https://via.placeholder.com/150",
                    menuAvailable = false
                ),
                isAddOn = true,
                onDelete = {},
                onShowSnackbar = {}
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun PreviewMenuListScreenTablet() {
    MenuListScreen()
}

//addon
@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
@Composable
fun PreviewAddOnListScreenTablet() {
    AddOnListScreen()
}