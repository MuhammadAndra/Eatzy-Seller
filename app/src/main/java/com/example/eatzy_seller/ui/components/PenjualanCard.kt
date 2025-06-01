// ui/components/PenjualanCard.kt
package com.example.eatzy_seller.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatzy_seller.ui.screen.profile.SalesViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PenjualanCard(viewModel: SalesViewModel, canteenId: Int) {
    val bulanList = listOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )

    val calendar = Calendar.getInstance()
    val currentMonthIndex = calendar.get(Calendar.MONTH) // 0-based (0-11)
    val currentYear = calendar.get(Calendar.YEAR)
    var selectedMonth by remember { mutableStateOf(bulanList[currentMonthIndex]) }
    var selectedMonthIndex by remember { mutableStateOf(currentMonthIndex) }
    var selectedYear by remember { mutableStateOf(currentYear) }
    var isDialogOpen by remember { mutableStateOf(false) }

    // Fetch sales only if canteenId is valid
    LaunchedEffect(canteenId, selectedMonthIndex, selectedYear) {
        if (canteenId > 0) {
            viewModel.fetchMonthlySales(canteenId, selectedMonthIndex + 1, selectedYear) // +1 for 1-based month
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .clickable { if (canteenId > 0) isDialogOpen = true }
            .padding(16.dp)
    ) {
        Text(
            text = "Monthly Sales",
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (canteenId == 0) {
            Text(
                text = "Waiting for canteen ID...",
                fontSize = 14.sp,
                color = Color.Gray
            )
        } else if (viewModel.isLoading.value) {
            Text(
                text = "Loading sales data...",
                fontSize = 14.sp
            )
        } else if (viewModel.error.value != null) {
            Text(
                text = "Error: ${viewModel.error.value}",
                fontSize = 14.sp,
                color = Color.Red
            )
        } else {
            Text(
                text = "Pendapatan bulan $selectedMonth $selectedYear: ${viewModel.income.value}",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }

    if (isDialogOpen) {
        var tempMonth by remember { mutableStateOf(selectedMonth) }
        var tempMonthIndex by remember { mutableStateOf(selectedMonthIndex) }
        var tempYear by remember { mutableStateOf(selectedYear.toString()) }

        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedMonth = tempMonth
                    selectedMonthIndex = tempMonthIndex
                    selectedYear = tempYear.toIntOrNull() ?: currentYear
                    isDialogOpen = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { isDialogOpen = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Pilih Bulan dan Tahun") },
            text = {
                Column {
                    // Dropdown bulan
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = tempMonth,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Bulan") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            bulanList.forEachIndexed { index, bulan ->
                                DropdownMenuItem(
                                    text = { Text(bulan) },
                                    onClick = {
                                        tempMonth = bulan
                                        tempMonthIndex = index
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Input tahun with validation
                    OutlinedTextField(
                        value = tempYear,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                tempYear = newValue
                            }
                        },
                        label = { Text("Tahun") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        )
    }
}