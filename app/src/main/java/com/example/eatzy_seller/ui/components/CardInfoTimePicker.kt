package com.example.eatzy_seller.ui.components

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import java.util.Calendar

@Composable
fun CardInfoTimePicker(
    icon: ImageVector,
    title: String,
    jamBuka: String,
    jamTutup: String,
    onJamBukaSelected: (String) -> Unit,
    onJamTutupSelected: (String) -> Unit,
    context: Context
) {
    val calendar = Calendar.getInstance()

    val openBothTimePicker = {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Pilih jam buka
        TimePickerDialog(context, { _, selectedHour, selectedMinute ->
            val buka = String.format("%02d:%02d", selectedHour, selectedMinute)
            onJamBukaSelected(buka)

            // Setelah jam buka dipilih, lanjutkan ke jam tutup
            TimePickerDialog(context, { _, tutupHour, tutupMinute ->
                val tutup = String.format("%02d:%02d", tutupHour, tutupMinute)
                onJamTutupSelected(tutup)
            }, hour, minute, true).show()

        }, hour, minute, true).show()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Column (modifier = Modifier.padding(0.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelSmall)
            TextButton(
                onClick = openBothTimePicker,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "$jamBuka - $jamTutup", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

