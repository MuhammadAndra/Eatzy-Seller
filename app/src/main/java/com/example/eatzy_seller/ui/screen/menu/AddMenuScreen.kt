package com.example.eatzy_seller.ui.screen.menu

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.eatzy_seller.navigation.navGraph.AddMenu
import com.example.eatzy_seller.navigation.navGraph.EditKategori
import com.example.eatzy_seller.ui.components.BottomNavBar
import com.example.eatzy_seller.ui.components.TopBarMenu
import com.example.eatzy_seller.ui.theme.SecondColor
import coil.compose.rememberImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMenuScreen(
    navController: NavController = rememberNavController()
) {
    // Menyimpan input user
    var namaMenu by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }

    // List kategori yang sudah ada
    val kategoriList = remember { mutableStateListOf("Makanan", "Minuman", "Dessert") }
    var selectedKategori by remember { mutableStateOf(kategoriList.firstOrNull() ?: "") }
    //var newKategori by remember { mutableStateOf("") }

    // List kategori add-on yang ditambahkan oleh pengguna
    val kategoriAddOnList = remember { mutableStateListOf<Pair<String, Boolean>>() }

    // State untuk dialogkategori
    var isDropdownExpanded by remember { mutableStateOf(false) }

    //add on
    var isAddOnDialogVisible by remember { mutableStateOf(false) }
    var newKategoriAddOn by remember { mutableStateOf("") }

    //toogle addon
    var isSingleChoice by remember { mutableStateOf(false) }


    Scaffold(
        containerColor = Color.White,
        topBar = { TopBarMenu(title = "Daftar Menu", navController = navController) },
        bottomBar = {
            Column {
                // Simpan Button
                SimpanTambahMenuButton {
                    navController.navigate(AddMenu)
                }
                Spacer(modifier = Modifier.height(3.dp))
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        // Form Input
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = namaMenu,
                onValueChange = { namaMenu = it },
                label = { Text("Nama Menu") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = { Text("Deskripsi") },
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

            // Kategori Dropdown
            Text("Kategori", fontSize = 18.sp, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(6.dp))
            // Button untuk menambah kategori baru yang akan memunculkan dialog
            Row(verticalAlignment = Alignment.CenterVertically) {
                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded,
                    onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedKategori,
                        onValueChange = {},
                        //label = { Text("Pilih Kategori") },
                        readOnly = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                                tint = Color(0xFFFFA726) // Warna oranye
                            )
                        },
                        placeholder = { Text("Pilih kategori") }, // Agar teks awal terlihat
                        singleLine = true,
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray,
                            disabledBorderColor = Color.Gray,
                            focusedTrailingIconColor = Color(0xFFFFA726),
                            unfocusedTrailingIconColor = Color(0xFFFFA726)
                        )

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
                IconButton(onClick = {
                    navController.navigate(EditKategori)
                }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit kategori", tint = SecondColor)
                }
            }

            //AddOn
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
                        .size(20.dp) // ukuran total lingkaran (sesuaikan dengan desain)
                        .background(Color(0xFFFFA726), shape = CircleShape)
                        .clickable { isAddOnDialogVisible = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah kategori",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp) // ukuran ikon di dalam
                    )
                }


            }


            // Tampilkan daftar kategori add-on yang sudah ditambahkan dalam bentuk Row mirip TextField
            kategoriAddOnList.forEachIndexed { index, (nama, isSingle) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    // Mirip TextField tapi tidak bisa diedit
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

                    // Tombol edit dengan ikon dalam kotak oranye
                    IconButton(
                        onClick = {
                            navController.navigate("edit_kategori_addon/${nama}/${isSingle}")
                        }
                    ){
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = SecondColor
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Upload Gambar
            Text("Tambah Gambar", fontSize = 18.sp, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(6.dp))
            UploadImageComponent()

            Spacer(modifier = Modifier.height(24.dp))


        }
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
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SecondColor)
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                Button(onClick = { isAddOnDialogVisible = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9D9D9))
                ) {
                    Text("Batal", color = Color.Black )
                }
            }
        )
    }
}

@Composable
fun SimpanTambahMenuButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE89D3C)), // Warna oranye
            shape = MaterialTheme.shapes.large.copy(all = CornerSize(50)), // Membulat penuh
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(56.dp)
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

@Composable
fun UploadImageComponent() {
    // State untuk menyimpan gambar yang dipilih
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Setup ActivityResultLauncher untuk memilih gambar dari galeri
    val context = LocalContext.current
    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Setelah gambar dipilih, simpan URI-nya
        selectedImageUri = uri
    }

    // Tombol untuk memilih gambar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
            .clickable {
                // Menjalankan launcher untuk memilih gambar dari galeri
                getContent.launch("image/*")
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_camera), // Ganti dengan ikon kamera
                contentDescription = "Upload Foto",
                tint = Color.Gray
            )
            Text("Unggah foto", color = Color.Gray)
        }

        // Jika ada gambar yang dipilih, tampilkan gambar tersebut
        selectedImageUri?.let {
            Image(painter = rememberImagePainter(it), contentDescription = null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUploadImage() {
    UploadImageComponent()
}

@Preview(showBackground = true)
@Composable
fun PreviewMenuFormScreen() {
    MaterialTheme {
        AddMenuScreen()
    }
}
