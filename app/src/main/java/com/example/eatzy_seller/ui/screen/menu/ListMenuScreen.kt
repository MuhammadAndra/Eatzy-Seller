package com.example.eatzy_seller.ui.screen.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eatzy_seller.R

@Composable
fun MenuListScreen() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        MenuItem(
            title = "Menu 1",
            price = "Rp 12.000",
            imageRes = R.drawable.ic_launcher_foreground
        )
        Spacer(modifier = Modifier.height(8.dp))
        MenuItem(
            title = "Menu 2",
            price = "Rp 13.000",

            imageRes = R.drawable.ic_launcher_foreground
        )
        Spacer(modifier = Modifier.height(8.dp))
        MenuItem(
            title = "Menu 3",
            price = "Rp 12.000",
            imageRes = R.drawable.ic_launcher_foreground
        )
        Spacer(modifier = Modifier.height(8.dp))
        MenuItem(
            title = "Menu 4",
            price = "Rp 12.000",
            imageRes = R.drawable.ic_launcher_foreground
        )
        Spacer(modifier = Modifier.height(8.dp))
        MenuItem(
            title = "Menu 5",
            price = "Rp 11.000",
            imageRes = R.drawable.ic_launcher_foreground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Add menu functionality */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Menu")
        }
    }
}

@Composable
fun MenuItem(title: String, price: String, imageRes: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium.copy(CornerSize(8.dp)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Menu Image",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = title)
                Text(text = price)
            }
            Column(modifier = Modifier.padding(0.dp)) {
                IconButton(onClick = { /* Handle visibility toggle */ }) {
                    Icon(Icons.Filled.Visibility, contentDescription = "Visibility")
                }
                IconButton(onClick = { /* Handle edit */ }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = { /* Handle delete */ }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMenuList() {
    MenuListScreen()
}
