package com.example.eatzy_seller.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eatzy_seller.navigation.navGraph.Home
import com.example.eatzy_seller.ui.theme.PrimaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarMenu(
    title: String,
    navController: NavController,
    showBackButton: Boolean = true
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, color = PrimaryColor, fontWeight = FontWeight.SemiBold) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = {
                    val navigatedBack = navController.popBackStack()
                    if (!navigatedBack) {
                        navController.navigate(Home::class.qualifiedName!!) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black
        )
    )
}

@Composable
fun DeleteMenuDialog(
    objek: String,
    title: String,
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hapus $objek") },
        text = { Text("Apakah Anda yakin ingin menghapus $objek \"$title\"?") },
        confirmButton = {
            TextButton(onClick = {
                onConfirmDelete()
                onDismiss()
            }) {
                Text("Hapus", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
