package com.example.eatzy_seller.ui.screen.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
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
import com.example.eatzy_seller.navigation.navGraph.AddAddOnCategory
import com.example.eatzy_seller.navigation.navGraph.AddMenu
import com.example.eatzy_seller.ui.components.*
import com.example.eatzy_seller.ui.theme.DeleteColor
import com.example.eatzy_seller.ui.theme.SecondColor
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

enum class MenuType {
    REGULAR_MENU, ADD_ON
}

//===========General===========//
@Composable
fun MenuManagementScreen(navController: NavController) {
    //default menu screen ke Menu
    var currentMenuType by remember { mutableStateOf(MenuType.REGULAR_MENU) }

    val viewModel: MenuViewModel = viewModel()

    //===========Pindah Menu dan Add On===========//
    when (currentMenuType) {
        MenuType.REGULAR_MENU -> MenuListScreen(
            navController = navController,
            onSwitchToAddOn = { currentMenuType = MenuType.ADD_ON },
            viewModel = viewModel
        )

        MenuType.ADD_ON -> AddOnListScreen(
            navController = navController,
            onSwitchToMenu = { currentMenuType = MenuType.REGULAR_MENU }
        )
    }
}

@Composable
fun ButtonStatus(
    currentScreen: String,
    onMenuClick: () -> Unit,
    onAddOnClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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

//===========Menu Screen===========//
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuListScreen(
    navController: NavController,
    onSwitchToAddOn: () -> Unit = {},
    viewModel: MenuViewModel = viewModel()
) {
    // Untuk snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Untuk edit dialog kategori
    var isEditDialogVisible by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<MenuCategory?>(null) }
    var showDeleteCategoryDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<MenuCategory?>(null) }

    val menuCategories by viewModel.menuCategories.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchMenus()

    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                TopBarMenu(
                    title = "Daftar Menu",
                    navController = navController,
                    showBackButton = false
                )
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
                onClick = { navController.navigate(AddMenu) }, // ke addMenuScreen
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
        //===========Konten Menu===========//
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + 80.dp // Tambahkan ekstra padding untuk FAB
            ),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            //===========Loop Kategori Menu===========//
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
                        //===========Nama Menu Kategori===========//
                        Text(
                            text = category.categoryName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 8.dp)
                        )
                        //garis
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = Color(0xFFBDBDBD),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        if (category.menus.isNullOrEmpty()) {
                            Text(
                                text = "Menu kosong",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            category.menus.forEachIndexed { index, menu ->
                                //===========Masuk ke Item Menu===========//
                                MenuItem(
                                    menu = menu,
                                    isAddOn = false,
                                    onShowSnackbar = { message ->
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar(message)
                                        }
                                    },
                                    navController = navController,
                                    menuViewModel = viewModel
                                )
                                HorizontalDivider(
                                    thickness = 0.5.dp,
                                    color = Color(0xFFBDBDBD),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            //===========Button edit Kategori Menu===========//
                            Button(
                                onClick = {
                                    categoryToEdit = category
                                    isEditDialogVisible = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SecondColor),
                                shape = MaterialTheme.shapes.large.copy(all = CornerSize(50)),
                            ) {
                                Text(
                                    text = "Edit Kategori",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            //===========Button delete Kategori Menu===========//
                            Button(
                                onClick = {
                                    categoryToDelete = category
                                    showDeleteCategoryDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DeleteColor),
                                shape = MaterialTheme.shapes.large.copy(all = CornerSize(50)),
                            ) {
                                Text(
                                    text = "Hapus",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        //===========Dialog edit kategori Menu===========//
        if (isEditDialogVisible && categoryToEdit != null) {
            EditCategoryDialog(
                initialName = categoryToEdit!!.categoryName,
                onDismiss = {
                    isEditDialogVisible = false
                    categoryToEdit = null
                },
                onSave = { newName ->
                    viewModel.updateCategoryName(categoryToEdit!!.idCategory, newName)
                    isEditDialogVisible = false
                    categoryToEdit = null

                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Kategori berhasil diubah")
                    }
                }
            )
        }

        //===========Dialog delete kategori Menu===========//
        if (showDeleteCategoryDialog && categoryToDelete != null) {
            DeleteDialog(
                objek = "Kategori Menu",
                title = categoryToDelete!!.categoryName,
                onConfirmDelete = {
                    viewModel.deleteCategory(categoryToDelete!!.idCategory)
                    showDeleteCategoryDialog = false
                    categoryToDelete = null

                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Kategori berhasil dihapus")
                    }
                },
                onDismiss = {
                    showDeleteCategoryDialog = false
                    categoryToDelete = null
                }
            )
        }
    }
}

//===========Item Menu===========//
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItem(
    menu: Menu,
    isAddOn: Boolean,
    onShowSnackbar: (String) -> Unit,
    navController: NavController,
    menuViewModel: MenuViewModel = viewModel()
)  {
    //===========Button Delete Item Menu===========//
    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        DeleteDialog(
            objek = "Menu",
            title = menu.menuName,
            onConfirmDelete = {
                menuViewModel.deleteMenu(menu.menuId)
                onShowSnackbar("Menu ${menu.menuName}\" berhasil dihapus")
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    //ubah biar jadi int
    val formattedPrice = NumberFormat.getNumberInstance(Locale("id", "ID")).format(menu.menuPrice)

    Row(verticalAlignment = Alignment.Top) {
        //===========Gambar Item Menu===========//
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

        //===========Deskripsi Item Menu===========//
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
            //===========Checkbox untuk buka tutup Menu===========//
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = menu.menuAvailable,
                    onCheckedChange = { isChecked ->
                        menuViewModel.toggleMenuAvailability(menu.menuId, isChecked)
                        onShowSnackbar("${menu.menuName} ${if (isChecked) "tersedia" else "tidak tersedia"}")
                    }
                )
                Text(
                    text = "Tampilkan Menu",
                    fontSize = 14.sp
                )
            }
        }

        //===========Button edit dan delete item Menu===========//
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(35.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit",
                tint = Color(0xFF3F5185),
                modifier = Modifier
                    .size(20.dp)
                    .clickable {navController.navigate("edit_menu/${menu.menuId}")
                    }
            )
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
                tint = DeleteColor,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { showDeleteDialog = true }
            )
        }
    }
}

//===========Add On Screen===========//
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AddOnListScreen(
    navController: NavController,
    onSwitchToMenu: () -> Unit = {},
    viewModel: MenuViewModel = viewModel()
) {
    // For snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // For dialogs
    var isEditDialogVisible by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<AddOnCategory?>(null) }
    var showDeleteCategoryDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<AddOnCategory?>(null) }

    val addOnCategories by viewModel.addonCategories.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAddons()
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                TopBarMenu(
                    title = "Daftar Add-On",
                    navController = navController,
                    showBackButton = false
                )
                // Switch button between menu and add-on
                ButtonStatus(
                    currentScreen = "Add-On",
                    onMenuClick = onSwitchToMenu,
                    onAddOnClick = { /* already on add-on */ }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }, //pop up
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
        //===========Konten kategori Add On===========//
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + 80.dp // Extra padding buat FAB
            ),
            modifier = Modifier.padding(horizontal = 8.dp)
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
                        //===========Add On Kategori ( nama & multiple )===========//
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = category.addOnCategoryName,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                            )
                            Text(
                                text = if (category.addOnCategoryMultiple) "Multiple" else "Single",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        //garis
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = Color(0xFFBDBDBD),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        if (category.addOns.isEmpty()) {
                            Text(
                                text = "Add-on kosong",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            //===========Loop Kategori Add On===========//
                            category.addOns.forEachIndexed { index, addOn ->
                                //masuk fun item add on
                                AddOnItem(
                                    addOn = addOn,
                                    onDelete = { /* delete logic here */ },
                                    onShowSnackbar = { message ->
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar(message)
                                        }
                                    }
                                )
                                //garis
                                HorizontalDivider(
                                    thickness = 0.5.dp,
                                    color = Color(0xFFBDBDBD),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )

                            }
                        }

                        //===========Button Kategori Add On===========//
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                //edit
                                onClick = {
                                    navController.navigate("edit_addOnCategory/${category.addOnCategoryId}")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SecondColor),
                                shape = MaterialTheme.shapes.large.copy(all = CornerSize(50)),
                            ) {
                                Text(
                                    text = "Edit Kategori",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                //delete
                                onClick = {
                                    categoryToDelete = category
                                    showDeleteCategoryDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DeleteColor),
                                shape = MaterialTheme.shapes.large.copy(all = CornerSize(50)),
                            ) {
                                Text(
                                    text = "Hapus",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        //===========Delete Category Add-On Dialog===========//
        if (showDeleteCategoryDialog && categoryToDelete != null) {
            DeleteDialog(
                objek = "Kategori Add-On",
                title = categoryToDelete!!.addOnCategoryName,
                onConfirmDelete = {
                    viewModel.deleteAddonCategory(categoryToDelete!!.addOnCategoryId)
                    showDeleteCategoryDialog = false
                    categoryToDelete = null

                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Kategori berhasil dihapus")
                    }
                },
                onDismiss = {
                    showDeleteCategoryDialog = false
                    categoryToDelete = null
                }
            )
        }
    }
}

//===========Add On Item===========//
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AddOnItem(
    addOn: AddOn?,
    onDelete: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    addonViewModel: MenuViewModel = viewModel()
) {
    if (addOn == null) {
        return // Atau bisa tampilkan UI placeholder
    }

    //delete item add on Dialog
    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        DeleteDialog(
            objek = "Add-On",
            title = addOn.AddOnName,
            onConfirmDelete = {
                addonViewModel.deleteAddon(addOn.AddOnId)
                onShowSnackbar("Add-On \"${addOn.AddOnName}\" berhasil dihapus")
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    //edit item addON Dialog
    var showEditDialog by remember { mutableStateOf(false) }
    //simpan data edit
    var newNamaAddOn by remember { mutableStateOf("") }
    var newHargaAddOn by remember { mutableStateOf(0.0) }

    if (showEditDialog) {
        Add_AddOnDialog(
            newAddOn = newNamaAddOn,
            harga = newHargaAddOn,
            AddOnNamaChange = { newNamaAddOn = it },
            AddOnHargaChange = { newHargaAddOn = it.toDouble() },
            onConfirm = {
                //kurang logika update edit
                onShowSnackbar("Add-On \"${addOn.AddOnName}\" berhasil diubah")
            },
            onDismiss = { showEditDialog = false }
        )
    }

    val formattedPrice = NumberFormat.getNumberInstance(Locale("id", "ID")).format(addOn.AddOnPrice)

    Row(verticalAlignment = Alignment.Top) {
        //===========Add On Item ( Nama dan Harga) ===========//
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
                    onCheckedChange = { isChecked ->
                        addonViewModel.toggleAddonAvailability(addOn.AddOnId, isChecked)
                        onShowSnackbar("${addOn.AddOnName} ${if (isChecked) "tersedia" else "tidak tersedia"}")
                    }
                )
                Text(
                    text = "Tampilkan Add-On",
                    fontSize = 14.sp
                )
            }
        }

        //===========Button Edit & Delete Item Add On===========//
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(35.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            Icon( //edit button item add-on
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit",
                tint = Color(0xFF3F5185),
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        newNamaAddOn = addOn.AddOnName
                        newHargaAddOn = addOn.AddOnPrice
                        showEditDialog = true
                    }
            )
            Icon( // delete button item add-on
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
                tint = DeleteColor,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { showDeleteDialog = true }
            )
        }
    }
}


//===========Preview===========//
@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewMenuManagementScreen() {
    val navController = rememberNavController()
    MenuManagementScreen(navController = navController)
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

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewAddOnListScreen() {
    val navController = rememberNavController()
    AddOnListScreen(navController = navController)
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
                onShowSnackbar = {}
            )
            AddOnItem(
                addOn = AddOn(
                    AddOnId = 2,
                    AddOnName = "Spicy Level 2",
                    AddOnPrice = 0.0,
                    AddOnAvailable = false
                ),
                onDelete = {},
                onShowSnackbar = {}
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewMenuListScreen() {
    val navController = rememberNavController()
    MenuListScreen(navController = navController)
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewMenuItem() {
//    val navController = rememberNavController()
//    Surface {
//        Column {
//            MenuItem(
//                menu = Menu(
//                    menuId = 1,
//                    menuName = "Nasi Goreng Spesial",
//                    menuPrice = 25000.0,
//                    menuImageRes = "https://via.placeholder.com/150",
//                    menuAvailable = true
//                ),
//                isAddOn = false,
//                onDelete = {},
//                onShowSnackbar = {},
//                navController = navController
//            )
//            MenuItem(
//                menu = Menu(
//                    menuId = 2,
//                    menuName = "Extra Keju",
//                    menuPrice = 5000.0,
//                    menuImageRes = "https://via.placeholder.com/150",
//                    menuAvailable = false
//                ),
//                isAddOn = true,
//                onDelete = {},
//                onShowSnackbar = {},
//                navController = navController
//            )
//        }
//    }
//}


