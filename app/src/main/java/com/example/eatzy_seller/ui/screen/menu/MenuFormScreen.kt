//package com.example.eatzy_seller.ui.screen.menu
//
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.Button
//import androidx.compose.material.Checkbox
//import androidx.compose.material.DropdownMenu
//import androidx.compose.material.DropdownMenuItem
//import androidx.compose.material.Icon
//import androidx.compose.material.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material.OutlinedButton
//import androidx.compose.material.OutlinedTextField
//import androidx.compose.material.Scaffold
//import androidx.compose.material.Text
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Image
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import coil.compose.rememberImagePainter
//import com.example.eatzy_seller.ui.components.TopBarMenu
//
//@Composable
//fun MenuFormScreen(
//    navController: NavController = rememberNavController(),
//    title: String = "Tambah Menu",
//    initialNamaMenu: String = "",
//    initialHarga: String = "",
//    initialEstimasi: String = "",
//    initialKategori: String = "",
//    initialAddOns: List<Pair<String, Boolean>> = emptyList(),
//    initialImageUri: Uri? = null,
//    onSave: (
//        namaMenu: String,
//        harga: String,
//        estimasi: String,
//        kategori: String,
//        addOns: List<Pair<String, Boolean>>,
//        imageUri: Uri?
//    ) -> Unit
//) {
//    var namaMenu by remember { mutableStateOf(initialNamaMenu) }
//    var harga by remember { mutableStateOf(initialHarga) }
//    var estimasi by remember { mutableStateOf(initialEstimasi) }
//
//    val kategoriList = listOf("Makanan", "Minuman", "Snack", "Paket")
//    var selectedKategori by remember { mutableStateOf(initialKategori.ifBlank { kategoriList.first() }) }
//
//    val kategoriAddOnList = remember { mutableStateListOf(*initialAddOns.toTypedArray()) }
//
//    var selectedImageUri by remember { mutableStateOf(initialImageUri) }
//
//    Scaffold(
//        topBar = {
//            TopBarMenu(title = title, navController = navController)
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .padding(innerPadding)
//                .padding(16.dp)
//                .verticalScroll(rememberScrollState())
//        ) {
//            UploadImageComponent(
//                selectedImageUri = selectedImageUri,
//                onImageSelected = { selectedImageUri = it }
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            InputMenuField(label = "Nama Menu", value = namaMenu, onValueChange = { namaMenu = it })
//            Spacer(modifier = Modifier.height(8.dp))
//            InputMenuField(label = "Harga", value = harga, onValueChange = { harga = it }, keyboardType = KeyboardType.Number)
//            Spacer(modifier = Modifier.height(8.dp))
//            InputMenuField(label = "Estimasi Waktu Masak (menit)", value = estimasi, onValueChange = { estimasi = it }, keyboardType = KeyboardType.Number)
//
//            Spacer(modifier = Modifier.height(8.dp))
//            PilihanKategoriDropDown(selectedKategori = selectedKategori, onKategoriSelected = { selectedKategori = it })
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text("Add-On", style = MaterialTheme.typography.titleMedium)
//            kategoriAddOnList.forEachIndexed { index, (text, checked) ->
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Checkbox(checked = checked, onCheckedChange = { isChecked ->
//                        kategoriAddOnList[index] = kategoriAddOnList[index].copy(second = isChecked)
//                    })
//                    Spacer(modifier = Modifier.width(8.dp))
//                    OutlinedTextField(
//                        value = text,
//                        onValueChange = { newText ->
//                            kategoriAddOnList[index] = newText to kategoriAddOnList[index].second
//                        },
//                        label = { Text("Nama Add-On") },
//                        modifier = Modifier.weight(1f)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    IconButton(onClick = { kategoriAddOnList.removeAt(index) }) {
//                        Icon(Icons.Default.Delete, contentDescription = "Hapus")
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//            OutlinedButton(onClick = {
//                kategoriAddOnList.add("" to false)
//            }) {
//                Icon(Icons.Default.Add, contentDescription = "Tambah Add-On")
//                Spacer(modifier = Modifier.width(4.dp))
//                Text("Tambah Add-On")
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(
//                onClick = {
//                    onSave(namaMenu, harga, estimasi, selectedKategori, kategoriAddOnList, selectedImageUri)
//                },
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Text("Simpan")
//            }
//        }
//    }
//}
//
//@Composable
//fun UploadImageComponent(
//    selectedImageUri: Uri?,
//    onImageSelected: (Uri?) -> Unit
//) {
//    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//        onImageSelected(uri)
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(150.dp)
//            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
//            .clickable { getContent.launch("image/*") },
//        contentAlignment = Alignment.Center
//    ) {
//        if (selectedImageUri != null) {
//            Image(
//                painter = rememberImagePainter(data = selectedImageUri),
//                contentDescription = "Selected Image",
//                modifier = Modifier.fillMaxSize()
//            )
//        } else {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Icon(
//                    imageVector = Icons.Default.Image,
//                    contentDescription = "Upload Foto",
//                    tint = Color.Gray
//                )
//                Text("Unggah foto", color = Color.Gray)
//            }
//        }
//    }
//}
//
//@Composable
//fun InputMenuField(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = { Text(label) },
//        modifier = Modifier.fillMaxWidth(),
//        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType)
//    )
//}
//
//@Composable
//fun PilihanKategoriDropDown(selectedKategori: String, onKategoriSelected: (String) -> Unit) {
//    val kategoriList = listOf("Makanan", "Minuman", "Snack", "Paket")
//    var expanded by remember { mutableStateOf(false) }
//
//    Box {
//        OutlinedTextField(
//            value = selectedKategori,
//            onValueChange = {},
//            readOnly = true,
//            label = { Text("Kategori") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { expanded = true }
//        )
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            kategoriList.forEach { kategori ->
//                DropdownMenuItem(onClick = {
//                    onKategoriSelected(kategori)
//                    expanded = false
//                }) {
//                    Text(kategori)
//                }
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MenuFormScreenPreview() {
//    MenuFormScreen(
//        title = "Tambah Menu",
//        initialNamaMenu = "Nasi Goreng",
//        initialHarga = "15000",
//        initialEstimasi = "10",
//        initialKategori = "Makanan",
//        initialAddOns = listOf("Telur" to true, "Kerupuk" to false),
//        initialImageUri = null,
//        onSave = { _, _, _, _, _, _ -> }
//    )
//}
//
