package com.example.eatzy_seller.ui.screen.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eatzy_seller.data.model.dummyAddOnCategories
import com.example.eatzy_seller.ui.components.BottomNavBar
import com.example.eatzy_seller.ui.components.TopBarMenu
import com.example.eatzy_seller.ui.theme.PrimaryColor
import com.example.eatzy_seller.ui.theme.SecondColor
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun AddOnCategoryScreen(
    navController: NavController = rememberNavController(),
    viewModel: ViewModel = MenuViewModel(), // atau sesuai ViewModel kamu
    categoryId: String,
    mode: AddOnMode = AddOnMode.ADD
) {
    // Dapatkan data dari ViewModel
    //val categoryState by viewModel.getAddOnCategoryById(categoryId).collectAsState(initial = null)

    // Local UI State
    var isDialogVisible by remember { mutableStateOf(false) }
    var newNamaAddOn by remember { mutableStateOf("") }
    var newHargaAddOn by remember { mutableStateOf("") }

    // Jika data belum siap, tampilkan loading
    if (categoryAddOn == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

//    val category = categoryState!!
//    var categoryName by remember { mutableStateOf(category.name) }
//    var isSingleChoice by remember { mutableStateOf(category.isSingleChoice) }
//    val addOnList = remember { mutableStateListOf<Pair<String, String>>().apply { addAll(category.addOns) } }

    
    // Lanjut seperti sebelumnya
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBarMenu(
                title = when (mode) {
                    AddOnMode.ADD -> if (categoryName.isBlank()) "Tambah Kategori" else "Kategori $categoryName"
                    AddOnMode.EDIT -> "Edit Kategori $categoryName"
                },
                navController = navController
            )
        },
        bottomBar = {
            Spacer(modifier = Modifier.height(10.dp))
            BottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp)
        ) {
            // Category Name Input
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Nama Kategori") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                singleLine = true,
                readOnly = mode == AddOnMode.EDIT
            )

            // Switch pilihan tunggal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 10.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Add-On Pilih salah satu?",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = isSingleChoice,
                        onCheckedChange = { isSingleChoice = it }
                    )
                }
            }

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
                        .clickable { isDialogVisible = true },
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

            Spacer(modifier = Modifier.height(10.dp))

            // Daftar Add-On in TextField-like format
            if (addOnList.isEmpty()) {
                Text(
                    text = "Belum ada Add-On",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                addOnList.forEachIndexed { index, (nama, harga) ->
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
                                text = "$nama - $harga",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                newNamaAddOn = nama
                                newHargaAddOn = harga
                                isDialogVisible = true
                                addOnList.removeAt(index)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = PrimaryColor
                            )
                        }

                        IconButton(
                            onClick = { addOnList.removeAt(index) }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Hapus",
                                tint = Color(0xFFFC2433)
                            )
                        }
                    }
                }
            }

            // Save Button
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    when (mode) {
                        AddOnMode.ADD -> { /* Handle add */ }
                        AddOnMode.EDIT -> { /* Handle edit */ }
                    }
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = categoryName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = SecondColor)
            ) {
                Text(
                    text = when (mode) {
                        AddOnMode.ADD -> "Simpan Kategori"
                        AddOnMode.EDIT -> "Simpan Perubahan"
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (isDialogVisible) {
            Add_AddOnDialog(
                newAddOn = newNamaAddOn,
                harga = newHargaAddOn,
                onAddOnChange = { newNamaAddOn = it },
                onHargaChange = { newHargaAddOn = it },
                onConfirm = {
                    if (newNamaAddOn.isNotBlank() && newHargaAddOn.isNotBlank()) {
                        addOnList.add(newNamaAddOn to newHargaAddOn)
                        newNamaAddOn = ""
                        newHargaAddOn = ""
                        isDialogVisible = false
                    }
                },
                onDismiss = {
                    isDialogVisible = false
                    newNamaAddOn = ""
                    newHargaAddOn = ""
                }
            )
        }
    }
}

@Composable
private fun Add_AddOnDialog(
    newAddOn: String,
    harga: String,
    onAddOnChange: (String) -> Unit,
    onHargaChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Add-On Baru") },
        text = {
            Column {
                OutlinedTextField(
                    value = newAddOn,
                    onValueChange = onAddOnChange,
                    label = { Text("Nama Add-On") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = harga,
                    onValueChange = onHargaChange,
                    label = { Text("Harga") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = newAddOn.isNotBlank() && harga.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = SecondColor)
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9D9D9))
            ) {
                Text("Batal", color = Color.Black)
            }
        }
    )
}

enum class AddOnMode {
    ADD, EDIT
}

@Preview(showBackground = true, name = "Add Mode Preview")
@Composable
fun PreviewAddOnCategoryScreen_AddMode() {
    val dummyNavController = rememberNavController()
    AddOnCategoryScreen(
        navController = dummyNavController,
        mode = AddOnMode.ADD
    )
}

@Preview(showBackground = true, name = "Edit Mode Preview")
@Composable
fun PreviewAddOnCategoryScreen_EditMode() {
    val dummyNavController = rememberNavController()
    AddOnCategoryScreen(
        navController = dummyNavController,
        mode = AddOnMode.EDIT,
        initialCategoryName = "Sambal",
        initialIsSingleChoice = true,
        initialAddOns = listOf(
            "Pedas" to "Rp 5.000",
            "Manis" to "Rp 3.000",
            "Asin" to "Rp 4.000"
        )
    )
}