package com.example.eatzy_seller.ui.screen.menu

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.eatzy_seller.data.model.AddOnCategory
import com.example.eatzy_seller.data.model.MenuCategory
import com.example.eatzy_seller.navigation.navGraph.AddMenu
import com.example.eatzy_seller.navigation.navGraph.EditCategory
import com.example.eatzy_seller.ui.components.*
import com.example.eatzy_seller.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMenuScreen(
    navController: NavController,
    menuId: Int,
    viewModel: MenuViewModel = viewModel()
) {
    LaunchedEffect(menuId) {
        viewModel.fetchMenus()
        viewModel.fetchAddons()
    }

    // Observe data from ViewModel
    val kategoriList by viewModel.menuCategories.collectAsState()
    val kategoriAddOnList by viewModel.addonCategories.collectAsState()
    val menu = remember(kategoriList) {
        kategoriList.flatMap { it.menus.orEmpty() }
            .find { it.menuId == menuId }
    }

    // Initialize states
    var namaMenu by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var estimasi by remember { mutableStateOf("") }
    var selectedKategori by remember { mutableStateOf<MenuCategory?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val selectedAddOns = remember { mutableStateListOf<AddOnCategory>() }

    // State untuk UI
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val isAddOnDialogVisible = remember { mutableStateOf(false) }
    val showFormDialog = remember { mutableStateOf(false) }
    var newKategoriAddOn by remember { mutableStateOf("") }
    var isSingleChoice by remember { mutableStateOf(false) }

    LaunchedEffect(kategoriList, menu) {
        if (menu != null && selectedKategori == null) {
            // Cari kategori yang mengandung menu ini
            selectedKategori = kategoriList.find { category ->
                category.menus?.any { it.menuId == menu.menuId } == true
            }
        }
        menu?.listCategoryAddOn?.let { addons ->
            selectedAddOns.clear()
            selectedAddOns.addAll(addons)
        }
    }

    if (menu == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Menu tidak ditemukan") // atau Text("Menu tidak ditemukan")
        }
        return
    }
    Scaffold(
        containerColor = Color.White,
        topBar = { TopBarMenu(title = "Edit Menu", navController = navController)},
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Detail Menu",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(6.dp)
            )

            OutlinedTextField(
                value = menu.menuName,
                onValueChange = { namaMenu = it },
                label = { Text("Nama Menu") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = menu.menuPrice.toInt().toString(),
                onValueChange = { harga = it },
                label = { Text("Harga") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = menu.menuPreparationTime.toString(),
                onValueChange = { estimasi = it },
                label = { Text("Estimasi Pembuatan (Menit)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Kategori Dropdown
            Row(verticalAlignment = Alignment.CenterVertically) {
                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded,
                    onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedKategori?.categoryName ?: "",
                        onValueChange = {},
                        label = { Text("Pilih Kategori") },
                        readOnly = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                                tint = PrimaryColor
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray,
                            focusedTrailingIconColor = PrimaryColor,
                            unfocusedTrailingIconColor = PrimaryColor
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        kategoriList.forEach { kategori ->
                            DropdownMenuItem(
                                text = { Text(kategori.categoryName) },
                                onClick = {
                                    selectedKategori = kategori
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
                IconButton(onClick = {
                    navController.navigate(EditCategory)
                }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit kategori",
                        tint = PrimaryColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AddOn Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                Text(
                    text = "Kategori Add On",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(SecondColor, shape = CircleShape)
                        .clickable { isAddOnDialogVisible.value = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah kategori",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            if (selectedAddOns.isEmpty()) {
                Text(
                    text = "Belum ada Add-On",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                selectedAddOns.forEach { addonKategori->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Text(
                                text = "${addonKategori.addOnCategoryName} (${if (addonKategori.addOnCategoryMultiple) "Pilih satu" else "Bebas pilih"})",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                selectedAddOns.remove(addonKategori)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = DeleteColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Image Section
            Text(
                "Gambar Menu",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(6.dp)
            )
            EditImageComponent(
                currentImageUri = selectedImageUri,
                onImageSelected = { uri -> selectedImageUri = uri }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Simpan Button
            SimpanTambahMenuButton {
                navController.navigate(AddMenu)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Dialogs for AddOn management
    if (isAddOnDialogVisible.value) {
        PilihKategoriAddOnDialog(
            kategoriAddOnList = kategoriAddOnList,
            selectedAddOns = selectedAddOns,
            onDismiss = { isAddOnDialogVisible.value = false },
            onTambahKategoriClick = {
                showFormDialog.value = true
            }
        )
    }

    if (showFormDialog.value) {
        AddKategoriAddOnDialog(
            newKategoriAddOn = newKategoriAddOn,
            onKategoriChange = { newKategoriAddOn = it },
            isSingleChoice = isSingleChoice,
            onSingleChoiceChange = { isSingleChoice = it },
            onSave = {
//                if (newKategoriAddOn.isNotBlank() &&
//                    !kategoriAddOnList.any { it.first == newKategoriAddOn }
//                ) {
//                    kategoriAddOnList.add(Pair(newKategoriAddOn, isSingleChoice))
//                    newKategoriAddOn = ""
//                    isSingleChoice = false
//                }
//                showFormDialog.value = false
//                isAddOnDialogVisible.value = true
            },
            onDismiss = {
                showFormDialog.value = false
                isAddOnDialogVisible.value = true
            }
        )
    }
}

@Composable
fun EditImageComponent(
    currentImageUri: Uri?,
    onImageSelected: (Uri) -> Unit
) {
    val context = LocalContext.current

    val getContent = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (currentImageUri != null) {
            Image(
                painter = rememberImagePainter(currentImageUri),
                contentDescription = "Gambar Menu",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { getContent.launch("image/*") },
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(36.dp)
                    .background(PrimaryColor, CircleShape)
                    .clickable { getContent.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Gambar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { getContent.launch("image/*") }
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Upload Foto",
                    tint = Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
                Text("Unggah foto", color = Color.Gray)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEditMenuScreen() {
    EditMenuScreen(
        navController = rememberNavController(),
        menuId = 1
    )
}