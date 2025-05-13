package com.example.eatzy_seller.ui.screen.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eatzy_seller.ui.components.BottomNavBar
import com.example.eatzy_seller.ui.components.TopBarMenu
import com.example.eatzy_seller.ui.theme.PrimaryColor
import com.example.eatzy_seller.ui.theme.SecondColor

@Composable
fun AddOnItemScreen(
    namaAddOn: String,
    isSingleChoice: Boolean,
    navController: NavController = rememberNavController()
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    var newNamaAddOn by remember { mutableStateOf("") }
    var newHargaAddOn by remember { mutableStateOf("") }

    val addOnList = remember {
        mutableStateListOf(
            "Add 1" to "Rp xx.xxx",
            "Add 2" to "Rp xx.xxx",
            "Add 3" to "Rp xx.xxx"
        )
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBarMenu(
                title = "Kategori Add-On $namaAddOn",
                navController = navController
            )
        },
        bottomBar = {
            Spacer(modifier = Modifier.height(10.dp))
            BottomNavBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDialogVisible = true },
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp)
        ) {
            // Switch pilihan tunggal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 10.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White// Ganti sesuai keinginanmu
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
                        onCheckedChange = {} // opsional jika tidak diubah di halaman ini
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Daftar Add-On
            addOnList.forEachIndexed { index, (nama, harga) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 10.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White// Ganti sesuai keinginanmu
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Nama Add-On : $nama")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Harga : $harga")
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(5.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    // Edit logika di sini
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = PrimaryColor
                                )
                            }

                            IconButton(
                                onClick = {
                                    addOnList.removeAt(index)
                                },
                                modifier = Modifier.size(24.dp)
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
            Button(onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9D9D9))
            ) {
                Text("Batal", color = Color.Black )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewAddOnItemScreen() {
    val dummyNavController = rememberNavController()
    AddOnItemScreen(
        namaAddOn = "Sambal",
        isSingleChoice = false,
        navController = dummyNavController
    )
}