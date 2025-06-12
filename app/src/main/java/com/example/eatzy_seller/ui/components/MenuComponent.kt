package com.example.eatzy_seller.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eatzy_seller.data.model.AddOn
import com.example.eatzy_seller.data.model.AddOnCategory
import com.example.eatzy_seller.data.model.dummyAddOnCategories
import com.example.eatzy_seller.data.model.dummyAddOns1
import com.example.eatzy_seller.navigation.navGraph.Home
import com.example.eatzy_seller.ui.theme.PrimaryColor
import com.example.eatzy_seller.ui.theme.SecondColor

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
fun DeleteDialog(
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

@Composable
fun EditCategoryDialog(
    initialName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Kategori") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Nama Kategori") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(text)
            }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }

    )
}

@Composable
fun AddCategoryDialog(
    newKategori: String,
    onKategoriChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Kategori Baru") },
        text = {
            OutlinedTextField(
                value = newKategori,
                onValueChange = onKategoriChange,
                label = { Text("Nama Kategori") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = newKategori.isNotBlank()
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Batal")
            }
        }
    )
}


@Composable
fun AddKategoriAddOnDialog(
    newKategoriAddOn: String,
    onKategoriChange: (String) -> Unit,
    isSingleChoice: Boolean,
    onSingleChoiceChange: (Boolean) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Kategori Add-On") },
        text = {
            Column {
                OutlinedTextField(
                    value = newKategoriAddOn,
                    onValueChange = onKategoriChange,
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
                        onCheckedChange = onSingleChoiceChange
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(containerColor = SecondColor)
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9D9D9))
            ) {
                Text("Batal", color = Color.Black)
            }
        }
    )
}

@Composable
fun PilihKategoriAddOnDialog(
    kategoriAddOnList: List<AddOnCategory>,
    selectedAddOns: SnapshotStateList<AddOnCategory?>,
    onDismiss: () -> Unit,
    onTambahKategoriClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pilih Kategori Add-On") },
        text = {
            Column {
                kategoriAddOnList.forEach { addonCategory ->
                    OutlinedButton(
                        onClick = {
                            if (addonCategory !in selectedAddOns) {
                                selectedAddOns.add(addonCategory)
                            }
                            onDismiss()
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.Gray),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(addonCategory.addOnCategoryName)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol Tambah Kategori
                Button(
                    onClick = {
                        onDismiss()
                        onTambahKategoriClick()
                    }
                ) {
                    Text("+ Tambah Kategori")
                }
            }
        },
        confirmButton = {}
    )
}


@Composable
fun Add_AddOnDialog(
    newAddOn: String,
    harga: Double?,
    AddOnNamaChange: (String) -> Unit,
    AddOnHargaChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Add-On Baru") },
        text = {
            Column {
                OutlinedTextField(
                    value = newAddOn,
                    onValueChange = AddOnNamaChange,
                    label = { Text("Nama Add-On") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = harga?.toInt().toString(),
                    onValueChange = AddOnHargaChange,
                    label = { Text("Harga") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = newAddOn.isNotBlank() && harga.toString().isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = SecondColor)
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9D9D9))
            ) {
                Text("Batal", color = Color.Black)
            }
        }
    )
}

//===========Preview=====================
@Preview(showBackground = true)
@Composable
fun TopBarMenuPreview() {
    val navController = rememberNavController()
    TopBarMenu(
        title = "Menu Title",
        navController = navController,
        showBackButton = true
    )
}

@Preview(showBackground = true)
@Composable
fun DeleteMenuDialogPreview() {
    DeleteDialog(
        objek = "menu",
        title = "Nasi Goreng",
        onConfirmDelete = {},
        onDismiss = {}
    )
}

@Preview(showBackground = true)
@Composable
fun EditCategoryDialogPreview() {
    EditCategoryDialog(
        initialName = "Makanan",
        onDismiss = {},
        onSave = {}
    )
}

@Preview(showBackground = true)
@Composable
fun AddCategoryDialogPreview() {
    var newKategori by remember { mutableStateOf("") }
    AddCategoryDialog(
        newKategori = newKategori,
        onKategoriChange = { newKategori = it },
        onConfirm = {},
        onDismiss = {}
    )
}

@Preview(showBackground = true)
@Composable
fun AddKategoriAddOnDialogPreview() {
    var newKategoriAddOn by remember { mutableStateOf("Makanan") }
    var isSingleChoice by remember { mutableStateOf(false) }

    AddKategoriAddOnDialog(
        newKategoriAddOn = newKategoriAddOn,
        onKategoriChange = { newKategoriAddOn = it },
        isSingleChoice = isSingleChoice,
        onSingleChoiceChange = { isSingleChoice = it },
        onSave = {},
        onDismiss = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PilihKategoriAddOnDialogPreview() {
    val selectedAddOns = remember { mutableStateListOf<AddOnCategory?>() }

    PilihKategoriAddOnDialog(
        kategoriAddOnList = dummyAddOnCategories,
        selectedAddOns = selectedAddOns,
        onDismiss = {},
        onTambahKategoriClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun Add_AddOnDialogPreview_Filled() {
    var newAddOn by remember { mutableStateOf("Keju") }
    var harga by remember { mutableStateOf<Double?>(5000.0) }

    Add_AddOnDialog(
        newAddOn = newAddOn,
        harga = harga,
        AddOnNamaChange = { newAddOn = it },
        AddOnHargaChange = { text ->
            harga = text.toDoubleOrNull()
        },
        onConfirm = { },
        onDismiss = { }
    )
}


