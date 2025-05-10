package com.example.eatzy_seller.ui.screen.menu

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahMenuScreen(
    navController: NavController = rememberNavController()
) {
    // Menyimpan input user
    var namaMenu by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var tambahKategoriAddOn by remember { mutableStateOf("") }

    // List kategori yang sudah ada
    val kategoriList = remember { mutableStateListOf("Makanan", "Minuman", "Dessert") }
    var selectedKategori by remember { mutableStateOf(kategoriList.firstOrNull() ?: "") }
    var newKategori by remember { mutableStateOf("") }

    // List kategori add-on yang ditambahkan oleh pengguna
    val kategoriAddOnList = remember { mutableStateListOf<Pair<String, Boolean>>() }

    // State untuk dialog
    var isDialogVisible by remember { mutableStateOf(false) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    //add on
    var isAddOnDialogVisible by remember { mutableStateOf(false) }
    var newKategoriAddOn by remember { mutableStateOf("") }

    //toogle addon
    var isSingleChoice by remember { mutableStateOf(false) }


    // Form Input
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = namaMenu,
            onValueChange = { namaMenu = it },
            label = { Text("Nama Menu") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = deskripsi,
            onValueChange = { deskripsi = it },
            label = { Text("Deskripsi") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = harga,
            onValueChange = { harga = it },
            label = { Text("Harga") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Kategori Dropdown
        Text("Kategori", fontWeight = FontWeight.SemiBold)
        // Button untuk menambah kategori baru yang akan memunculkan dialog
        Row(verticalAlignment = Alignment.CenterVertically) {
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
                modifier = Modifier.weight(1f)
            ) {
                TextField(
                    value = selectedKategori,
                    onValueChange = {},
                    label = { Text("Pilih Kategori") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                //untuk liat dropdown
                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    kategoriList.forEach { kategori ->
                        androidx.compose.material3.DropdownMenuItem(
                            text = { Text(kategori) },
                            onClick = {
                                selectedKategori = kategori
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            IconButton(onClick = { isDialogVisible = true }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah kategori")
            }
        }

        // Kategori Add-On
        Text("Kategori Add-On", fontWeight = FontWeight.SemiBold)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = tambahKategoriAddOn,
                onValueChange = { tambahKategoriAddOn = it },
                label = { Text("Tambah Kategori Add-On") },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                isAddOnDialogVisible = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah kategori add-on")
            }
        }

        // Tampilkan daftar kategori add-on yang sudah ditambahkan dalam bentuk Card
        Column(modifier = Modifier.padding(top = 8.dp)) {
            kategoriAddOnList.forEachIndexed { index, (nama, isSingle) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Text(
                            text = "${index + 1}. $nama (${if (isSingle) "Pilih satu" else "Bebas pilih"})",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = {
                        navController.navigate("edit_kategori_addon/${nama}/${isSingle}")
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }


                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Upload Gambar
        Text("Tambah Gambar", fontWeight = FontWeight.SemiBold)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .clickable { /* Upload Foto */ },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Upload Foto", tint = Color.Gray)
                Text("Unggah foto", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Simpan Button
        Button(
            onClick = { /* Simpan data */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Simpan")
        }
    }

    // Dialog untuk menambah kategori
    if (isDialogVisible) {
        AlertDialog(
            onDismissRequest = { isDialogVisible = false },
            title = { Text("Tambah Kategori Baru") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newKategori,
                        onValueChange = { newKategori = it },
                        label = { Text("Kategori Baru") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newKategori.isNotBlank() && !kategoriList.contains(newKategori)) {
                            kategoriList.add(newKategori)
                            selectedKategori = newKategori
                            newKategori = ""
                        }
                        isDialogVisible = false
                    }
                ) {
                    Text("Tambah")
                }
            },
            dismissButton = {
                Button(onClick = { isDialogVisible = false }) {
                    Text("Batal")
                }
            }
        )
    }
    if (isAddOnDialogVisible) {
        AlertDialog(
            onDismissRequest = { isAddOnDialogVisible = false },
            title = { Text("Tambah Kategori Add-On") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newKategoriAddOn,
                        onValueChange = { newKategoriAddOn = it },
                        label = { Text("Nama Kategori Add-On") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add-On Pilih salah satu?", modifier = Modifier.weight(1f))
                        Switch(
                            checked = isSingleChoice,
                            onCheckedChange = { isSingleChoice = it }
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newKategoriAddOn.isNotBlank() &&
                            !kategoriAddOnList.any { it.first == newKategoriAddOn }
                        ) {
                            kategoriAddOnList.add(Pair(newKategoriAddOn, isSingleChoice))
                            newKategoriAddOn = ""
                            isSingleChoice = false
                        }
                        isAddOnDialogVisible = false
                    }
                ) {
                    Text("Tambah")
                }
            },
            dismissButton = {
                Button(onClick = { isAddOnDialogVisible = false }) {
                    Text("Batal")
                }
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewMenuFormScreen() {
    MaterialTheme {
        TambahMenuScreen()
    }
}
