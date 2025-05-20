package com.example.eatzy_seller.ui.screen.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eatzy_seller.data.model.AddOn
import com.example.eatzy_seller.data.model.dummyAddOnCategories
import com.example.eatzy_seller.data.model.dummyAddOns1
import com.example.eatzy_seller.data.model.fetchCategoryById
import com.example.eatzy_seller.ui.components.Add_AddOnDialog
import com.example.eatzy_seller.ui.components.BottomNavBar
import com.example.eatzy_seller.ui.components.TopBarMenu
import com.example.eatzy_seller.ui.theme.PrimaryColor
import com.example.eatzy_seller.ui.theme.SecondColor
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AddOnCategoryScreen(
    navController: NavController,
    mode: AddOnMode = AddOnMode.ADD,
    categoryId: Int? = null
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    var newNamaAddOn by remember { mutableStateOf("") }
    var newHargaAddOn by remember { mutableStateOf(0.0) }

    var categoryName by remember { mutableStateOf("") }
    var isSingleChoice by remember { mutableStateOf(false) }
    var addOnList = remember { mutableStateListOf<AddOn>() }

    // Simulasi ambil data jika mode edit
    LaunchedEffect(categoryId) {
        if (mode == AddOnMode.EDIT && categoryId != null) {
            val fetched = fetchCategoryById(categoryId)
            categoryName = fetched.addOnCategoryName
            isSingleChoice = fetched.addOnCategoryMultiple
            addOnList.clear()
            addOnList.addAll(fetched.addOns)
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBarMenu(
                title = when (mode) {
                    AddOnMode.ADD -> if (categoryName.isBlank()) "Tambah Kategori Add-On" else "Kategori ${categoryName}"
                    AddOnMode.EDIT -> "Edit Kategori ${categoryName}"
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
            // Input nama kategori
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Nama Kategori") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            // Switch pilihan tunggal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 10.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                Text(
                    text = "Item Add On",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 8.dp)
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
                addOnList.forEachIndexed { index, addOn ->
                    val formatted = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply {
                        maximumFractionDigits = 0
                    }.format(addOn.AddOnPrice)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Text(
                                text = addOn.AddOnName +" - "+ formatted,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                newNamaAddOn = addOn.AddOnName
                                newHargaAddOn = addOn.AddOnPrice
                                isDialogVisible = true
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

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    when (mode) {
                        AddOnMode.ADD -> {
                            // Simpan ke DB atau repo
                        }
                        AddOnMode.EDIT -> {
                            // Update ke DB atau repo
                        }
                    }
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = categoryName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = SecondColor),
                shape = MaterialTheme.shapes.large.copy(all = CornerSize(50))
            ) {
                Text(
                    text = when (mode) {
                        AddOnMode.ADD -> "Simpan Kategori"
                        AddOnMode.EDIT -> "Simpan Perubahan"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (isDialogVisible) {
            Add_AddOnDialog(
                newAddOn = newNamaAddOn,
                harga = newHargaAddOn,
                AddOnNamaChange = { newNamaAddOn = it },
                AddOnHargaChange = { newHargaAddOn = it.toDouble() },
                onConfirm = {
                    if (newNamaAddOn.isNotBlank() && newHargaAddOn != null) {
                        addOnList.add(
                            AddOn(
                                AddOnId = addOnList.size + 1,
                                AddOnName = newNamaAddOn,
                                AddOnPrice = newHargaAddOn,
                                AddOnAvailable = true
                            )
                        )
                        newNamaAddOn = ""
                        newHargaAddOn = 0.0
                        isDialogVisible = false
                    }
                },
                onDismiss = {
                    isDialogVisible = false
                    newNamaAddOn = ""
                    newHargaAddOn = 0.0
                }
            )

        }
    }
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
        categoryId = dummyAddOnCategories[0].addOnCategoryId
    )
}