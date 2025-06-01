// ui/screen/profile/ProfileScreen.kt
package com.example.eatzy_seller.ui.screen.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eatzy_seller.UserViewModel
import com.example.eatzy_seller.UserViewModelFactory
import com.example.eatzy_seller.ui.components.*
import kotlinx.coroutines.launch

@Preview
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    ProfileScreen(
        navController = navController,
        onNavigateToEdit = {},
        onLogout = {}
    )
}

@Composable
fun ProfileScreen(
    navController: NavController,
    onNavigateToEdit: () -> Unit,
    onLogout: () -> Unit
) {
    var isOpen by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var jamBuka by remember { mutableStateOf("00:00") }
    var jamTutup by remember { mutableStateOf("00:00") }
    var showEditDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    // Set up the UserViewModel and SalesViewModel
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(context))
    val salesViewModel: SalesViewModel = viewModel(factory = SalesViewModelFactory(context))

    // Get user state
    val userState by userViewModel.userState.collectAsState()
    var canteenId by remember { mutableStateOf<Int?>(null) }

    // Fetch user details and update canteenId reactively
    LaunchedEffect(userState) {
        if (userState.isLoading && userState.id == null) {
            userViewModel.fetchUserDetails()
        } else if (userState.id != null) {
            canteenId = userState.id
        }
    }

    // Show toast for errors
    LaunchedEffect(userState.error) {
        if (userState.error != null) {
            Toast.makeText(context, userState.error, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopNavBar(
            title = "Profil",
            onBackClick = { navController.popBackStack() }
        )
        Scaffold(
            bottomBar = { BottomNavBar(navController) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // User Info Section with Edit Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        if (userState.isLoading) {
                            Text(
                                text = "Loading...",
                                style = MaterialTheme.typography.titleMedium
                            )
                        } else {
                            Text(
                                text = userState.name ?: "Eli Diana",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Text(
                            text = userState.email ?: "Lalapan Mbak Eli",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Name",
                            tint = Color(0xFFF59A2F)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Switch Kantin Buka
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Kantin Buka", style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = isOpen,
                        onCheckedChange = { isOpen = it }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                CardInfoTimePicker(
                    icon = Icons.Default.AccessTime,
                    title = "Jam Operasional",
                    jamBuka = jamBuka,
                    jamTutup = jamTutup,
                    onJamBukaSelected = { jamBuka = it },
                    onJamTutupSelected = { jamTutup = it },
                    context = context
                )
                Spacer(modifier = Modifier.height(12.dp))

                EditableCardInfo(
                    icon = Icons.Default.LocationOn,
                    title = "Lokasi",
                    initialValue = "Kantin Fakultas Ilmu Komputer"
                )

                Spacer(modifier = Modifier.height(12.dp))

                HariBukaCard()

                Spacer(modifier = Modifier.height(12.dp))

                // Penjualan
                PenjualanCard(
                    viewModel = salesViewModel,
                    canteenId = canteenId ?: 0 // Fallback to 0 if null
                )

                Spacer(modifier = Modifier.weight(1f))

                // Tombol Keluar
                Button(
                    onClick = {
                        userViewModel.resetState()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = "Keluar", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Edit Name Dialog
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Nama") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Nama Baru") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newName.isNotBlank()) {
                            scope.launch {
                                userViewModel.updateUserName(newName)
                                showEditDialog = false
                                newName = ""
                            }
                        } else {
                            Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}