package com.example.eatzy_seller.ui.screen.menu

import android.net.Uri
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.eatzy_seller.navigation.navGraph.EditCategory
import com.example.eatzy_seller.ui.components.*
import com.example.eatzy_seller.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMenuScreen(
    navController: NavController,
    menuId: Int // Pass the menu ID from navigation
) {
    // State for menu data (would normally be loaded from ViewModel)
    var namaMenu by remember { mutableStateOf("Nasi Goreng Spesial") }
    var harga by remember { mutableStateOf("25000") }
    var estimasi by remember { mutableStateOf("15") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // List kategori yang sudah ada
    val kategoriList = remember { mutableStateListOf("Makanan", "Minuman", "Dessert") }
    var selectedKategori by remember { mutableStateOf("Makanan") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    // State for AddOns
    val isAddOnDialogVisible = remember { mutableStateOf(false) }
    val showFormDialog = remember { mutableStateOf(false) }
    val kategoriAddOnList = remember {
        mutableStateListOf(
            Pair("Level Pedas", true),
            Pair("Topping", false)
        )
    }
    val selectedAddOns = remember { mutableStateListOf("Level Pedas") }

    var newKategoriAddOn by remember { mutableStateOf("") }
    var isSingleChoice by remember { mutableStateOf(false) }

    // Load menu data when screen appears (simulated here)
    LaunchedEffect(menuId) {
        // In a real app, you would fetch menu data from ViewModel here
        // For example: viewModel.loadMenuData(menuId)
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
                value = namaMenu,
                onValueChange = { namaMenu = it },
                label = { Text("Nama Menu") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = harga,
                onValueChange = { harga = it },
                label = { Text("Harga") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = estimasi,
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
                        value = selectedKategori,
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
                                text = { Text(kategori) },
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

            if (kategoriAddOnList.isEmpty()) {
                Text(
                    text = "Belum ada Add-On",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                kategoriAddOnList.forEachIndexed { index, (nama, isSingle) ->
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
                                text = "$nama (${if (isSingle) "Pilih satu" else "Bebas pilih"})",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                kategoriAddOnList.removeAt(index)
                                selectedAddOns.remove(nama)
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

            // Save Button
            SaveEditMenuButton(
                onSave = {
                    // Save edited menu data
                    // viewModel.updateMenu(menuId, updatedData)
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )

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
                if (newKategoriAddOn.isNotBlank() &&
                    !kategoriAddOnList.any { it.first == newKategoriAddOn }
                ) {
                    kategoriAddOnList.add(Pair(newKategoriAddOn, isSingleChoice))
                    newKategoriAddOn = ""
                    isSingleChoice = false
                }
                showFormDialog.value = false
                isAddOnDialogVisible.value = true
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
                contentDescription = "Menu Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { getContent.launch("image/*") }
            )

            // Edit overlay
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
                    Icons.Default.Edit,
                    contentDescription = "Edit Image",
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
                    Icons.Default.CameraAlt,
                    contentDescription = "Upload Foto",
                    tint = Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
                Text("Unggah foto", color = Color.Gray)
            }
        }
    }
}

@Composable
fun SaveEditMenuButton(
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = DeleteColor
            ),
            border = BorderStroke(1.dp, DeleteColor),
            shape = MaterialTheme.shapes.large.copy(all = CornerSize(50))
        ) {
            Text(
                text = "Batal",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onSave,
            colors = ButtonDefaults.buttonColors(containerColor = SecondColor),
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = MaterialTheme.shapes.large.copy(all = CornerSize(50))
        ) {
            Text(
                text = "Simpan",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
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