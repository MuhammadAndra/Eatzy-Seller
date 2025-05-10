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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TambahMenu() {
    var namaMenu by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var kategoriAddOn by remember { mutableStateOf("Kategori Add On 1") }
    var tambahKategoriAddOn by remember { mutableStateOf("") }

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

        Text("Kategori", fontWeight = FontWeight.SemiBold)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = kategori,
                onValueChange = { kategori = it },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { /* Tambah kategori */ }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah kategori")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Kategori Add On", fontWeight = FontWeight.SemiBold)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = kategoriAddOn,
                onValueChange = { kategoriAddOn = it },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { /* Edit kategori add on */ }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit kategori add on")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = tambahKategoriAddOn,
                onValueChange = { tambahKategoriAddOn = it },
                label = { Text("Tambah Kategori Add-On") },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { /* Tambah add-on */ }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah kategori add-on")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //ubah
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
}
@Preview(showBackground = true)
@Composable
fun PreviewMenuFormScreen() {
    MaterialTheme {
        TambahMenu()
    }
}
