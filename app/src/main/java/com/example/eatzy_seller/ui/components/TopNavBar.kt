package com.example.eatzy_seller.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TopNavBar(title: String, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White) // Latar belakang putih
            .padding(vertical = 8.dp) // Padding vertikal agar tidak terlalu rapat
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween, // Tombol back di kiri dan title di tengah
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            // Letakkan Text di tengah
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF3C4A64),
                modifier = Modifier
                    .align(Alignment.CenterVertically) // Agar Text berada di tengah secara vertikal
                    .fillMaxWidth(), // Membuat Text mengisi seluruh lebar untuk memastikan di tengah
                textAlign = TextAlign.Center // Memastikan teks di tengah
            )

            Spacer(modifier = Modifier.width(48.dp)) // Menjaga jarak agar tidak terlalu rapat dengan title
        }
    }
}

@Preview
@Composable
fun TopNavBarPreview() {
    TopNavBar(
        title = "Profil",
        onBackClick = { /* Handle Back Button Click */ }
    )
}
