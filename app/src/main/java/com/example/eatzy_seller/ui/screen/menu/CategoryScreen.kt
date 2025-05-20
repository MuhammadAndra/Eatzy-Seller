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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eatzy_seller.ui.components.AddCategoryDialog
import com.example.eatzy_seller.ui.components.BottomNavBar
import com.example.eatzy_seller.ui.components.TopBarMenu
import com.example.eatzy_seller.ui.theme.PrimaryColor
import com.example.eatzy_seller.ui.theme.SecondColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavController = rememberNavController()
) {
    val kategoriList = remember { mutableStateListOf("Makanan", "Minuman", "Dessert") }
    var isDialogVisible by remember { mutableStateOf(false) }
    var newKategori by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White,
        topBar = { TopBarMenu(title = "List Kategori", navController = navController) },
        bottomBar = {
            BottomNavBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDialogVisible = true },
                containerColor = SecondColor,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, modifier = Modifier.size(35.dp) , contentDescription = "Tambah Kategori", tint = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .fillMaxSize()
        ) {

            // Content
            if (kategoriList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tidak ada kategori",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    itemsIndexed(kategoriList) { index, kategori ->
                        CategoryItem(
                            kategori = kategori,
                            onEditClick = { /* editKategori*/ },
                            onDeleteClick = { /* HapusKategori*/ },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }

    if (isDialogVisible) {
        AddCategoryDialog(
            newKategori = newKategori,
            onKategoriChange = { newKategori = it },
            onConfirm = {
                if (newKategori.isNotBlank() && !kategoriList.contains(newKategori)) {
                    kategoriList.add(newKategori)
                    newKategori = ""
                }
                isDialogVisible = false
            },
            onDismiss = { isDialogVisible = false }
        )
    }
}

@Composable
private fun CategoryItem(
    kategori: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = kategori,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onEditClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = PrimaryColor
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Hapus",
                    tint = Color(0xFFFC2433)
                )

            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewCategoryScreen() {
    MaterialTheme {
        CategoryScreen()
    }
}