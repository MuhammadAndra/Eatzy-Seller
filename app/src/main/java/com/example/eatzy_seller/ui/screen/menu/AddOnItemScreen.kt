package com.example.eatzy_seller.ui.screen.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun AddOnItemScreen(
    namaKategori: String,
    isSingleChoice: Boolean,
    navController: NavController = rememberNavController()
) {
    val addOnList = remember {
        mutableStateListOf(
            "Add 1" to "Rp xx.xxx",
            "Add 2" to "Rp xx.xxx",
            "Add 3" to "Rp xx.xxx"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Judul
        Text(
            text = "Kategori Add-On $namaKategori",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Switch pilihan tunggal
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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

        Spacer(modifier = Modifier.height(16.dp))

        // Daftar Add-On
        addOnList.forEachIndexed { index, (nama, harga) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Nama Add-On : $nama")
                        Text("Harga : $harga")
                    }
                    Column {
                        IconButton(onClick = {
                            // Edit logika di sini
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    
                        IconButton(onClick = {
                            addOnList.removeAt(index)
                        }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Hapus",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Tombol tambah add-on
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = {
                    // Tambah Add-On
                },
                containerColor = Color(0xFFFFA726)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAddOnItemScreen() {
    val dummyNavController = rememberNavController()
    AddOnItemScreen(
        namaKategori = "Add-On Sambal",
        isSingleChoice = false,
        navController = dummyNavController
    )
}