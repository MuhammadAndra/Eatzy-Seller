package com.example.eatzy_seller.ui.screen.orderDetail

import android.icu.text.NumberFormat
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatzy_seller.R
import com.example.eatzy_seller.data.model.Order
import com.example.eatzy_seller.data.model.OrderItem
import java.util.Locale

@Composable
fun OrderDetailScreen(order: Order, onNavigateToOrderFinished : () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Detail Pesanan",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF455E84),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("No. Pesanan", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                Text("${order.id}", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tanggal Pemesanan", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                Text("${order.date}", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }

        order.items.forEach {
            OrderItemCard(item = it)
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))

        val totalHarga = order.items.sumOf { it.quantity * it.price }
        OrderDetailRow(label = "Subtotal Pesanan (${order.items.size} item)", value = "${formatPrice(totalHarga)}")

        Spacer(modifier = Modifier.weight(1f))

        // button
        Button(
            onClick = onNavigateToOrderFinished,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFC9824)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Pesanan Selesai", color = Color.White)
        }
    }
}

fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
    return "Rp ${formatter.format(price)}"
}

@Composable
fun OrderDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun OrderItemCard(item: OrderItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.sample_food),
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("${formatPrice(item.price)}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Text("Jumlah: ${item.quantity}", fontSize = 14.sp)
            Text(item.addOn, fontSize = 14.sp)
            Text("Catatan: ${item.note}", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOrderDetailScreen() {
    val order = Order(
        id = 1,
        date = "13/03/2025",
        total = 25.000,
        items = listOf(
            OrderItem("Menu 1", 1, "sambalnya banyakin", "sambal bawang, makan di tempat", 12000.0),
            OrderItem("Menu 2", 1, "nasinya dikit aja","sambal tomat, bungkus",13000.0),
            OrderItem("Menu 3", 1, "sambalnya banyakin","sambal bawang, bungkus",13000.0)
        )
    )
    MaterialTheme {
        Surface {
            OrderDetailScreen(order = order, onNavigateToOrderFinished = {})
        }
    }
}
