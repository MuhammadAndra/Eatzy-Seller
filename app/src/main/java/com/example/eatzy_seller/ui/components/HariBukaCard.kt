package com.example.eatzy_seller.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HariBukaCard(
    modifier: Modifier = Modifier
) {
    val hariList = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
    val selectedDays = remember { mutableStateListOf<String>() }
    var isDialogOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .clickable { isDialogOpen = true }
            .padding(16.dp)
    ) {
        Text("Hari Buka", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        if (selectedDays.isNotEmpty()) {
            Text(
                selectedDays.joinToString(", "),
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            Text("Belum dipilih", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }

    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            confirmButton = {
                TextButton(onClick = { isDialogOpen = false }) {
                    Text("Selesai")
                }
            },
            title = { Text("Pilih Hari Buka") },
            text = {
                Column {
                    hariList.forEach { hari ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = selectedDays.contains(hari),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) selectedDays.add(hari)
                                    else selectedDays.remove(hari)
                                }
                            )
                            Text(text = hari)
                        }
                    }
                }
            }
        )
    }
}