package com.example.eatzy_seller.ui.screen.orderState

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatzy_seller.R
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.data.dummyOrders
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OrderListScreen(
    orders: List<OrderState>,
    selectedStatus: String,
    onStatusSelected: (String) -> Unit,
    onOrderAccepted: (OrderState) -> Unit,
    onOrderRejected: (OrderState) -> Unit,
    onOrderDetailed: (OrderState) -> Unit
) {
    val statuses = listOf("Semua", "Konfirmasi", "Proses", "Selesai", "Batal")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Pesanan",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF455E84),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
        )
        // Tab bar status filter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            statuses.forEach { status ->
                TextButton(onClick = { onStatusSelected(status) }) {
                    Text(
                        text = status,
                        color = if (status == selectedStatus) Color(0xFFFC9824) else Color.Gray,
                        fontWeight = if (status == selectedStatus) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val filteredOrders = if (selectedStatus == "Semua") {
            orders
        } else {
            orders.filter { it.status == selectedStatus }
        }

        LazyColumn {
            items(filteredOrders) { order ->
                OrderCard(
                    order = order,
                    onOrderAccepted = { onOrderAccepted(order) },
                    onOrderRejected = { onOrderRejected(order) },
                    onOrderDetailed = { onOrderDetailed(order) }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
    return "Rp ${formatter.format(price)}"
}

@Composable
fun OrderCard(order: OrderState, onOrderAccepted: (OrderState) -> Unit, onOrderRejected: (OrderState) -> Unit, onOrderDetailed: (OrderState) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
            .clickable { if (order.status == "Proses") onOrderDetailed(order) }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Pesanan #${order.id}", fontWeight = FontWeight.Bold)
            StatusPesanan(status = order.status)
        }

        Text("Dipesan pada ${order.date}", fontSize = 14.sp, color = Color.Gray)

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        order.items.forEachIndexed { index, item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.sample_food),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(item.name, fontWeight = FontWeight.Bold)
                        Text(formatPrice(item.price), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Text("Jumlah: ${item.quantity}", fontSize = 14.sp)
                    Text(item.addOn, fontSize = 14.sp)
                    Text("Catatan: ${item.note}", fontSize = 14.sp, color = Color.Gray)
                }
            }

            if (index == order.items.lastIndex) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                val total = order.items.sumOf { it.quantity * it.price }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text("Total: ${formatPrice(total)}", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (order.status == "Konfirmasi") {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onOrderAccepted(order) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFC9824))
                ) {
                    Text("Terima", color = Color.White)
                }
                Button(
                    onClick = { onOrderRejected(order) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF455E84))
                ) {
                    Text("Tolak", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun StatusPesanan(status: String) {
    val color = when (status) {
        "Proses" -> Color(0xFFFC9824)
        "Selesai" -> Color(0xFF4CAF50)
        "Batal" -> Color(0xFFF44336)
        "Konfirmasi" -> Color(0xFF1976D2)
        else -> Color.Gray
    }

    val bgColor = when (status) {
        "Proses" -> Color(0xFFFFF3E0)
        "Selesai" -> Color(0xFFE8F5E9)
        "Batal" -> Color(0xFFFFEBEE)
        "Konfirmasi" -> Color(0xFFE3F2FD)
        else -> Color(0xFFE0E0E0)
    }

    Text(
        text = status,
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(color = bgColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewOrderListScreen() {
    var selectedStatus by remember { mutableStateOf("Semua") }
    val dummyOrders = dummyOrders // dari file OrderDummyData.kt

    MaterialTheme {
        Surface {
            OrderListScreen(
                orders = dummyOrders,
                selectedStatus = selectedStatus,
                onStatusSelected = { selectedStatus = it },
                onOrderAccepted = { /* No-op in preview */ },
                onOrderRejected = { /* No-op in preview */ },
                onOrderDetailed = { /* No-op in preview */ }
            )
        }
    }
}