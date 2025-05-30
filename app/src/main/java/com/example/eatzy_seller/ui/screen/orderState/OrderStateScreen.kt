package com.example.eatzy_seller.ui.screen.orderState

import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.eatzy_seller.data.dummyOrders
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.token
import com.example.eatzy_seller.ui.components.BottomNavBar
import java.text.NumberFormat
import java.util.Locale

/**
 * Komponen TopNavBar untuk menampilkan header halaman dengan tombol kembali.
 */
@Composable
fun TopNavBar(
    title: String,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Kembali",
                tint = Color(0xFF455E84)
            )
        }
        Text(
            text = title,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF455E84),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(48.dp))
    }
}

/**
 * Menampilkan daftar pesanan yang difilter berdasarkan status.
 */
@Composable
fun OrderListScreen(
    navController: NavHostController,
    orders: List<OrderState>,
    selectedStatus: OrderStatus,
    onStatusSelected: (OrderStatus) -> Unit,
    onOrderAccepted: (OrderState) -> Unit,
    onOrderRejected: (OrderState) -> Unit,
    onOrderDetailed: (OrderState) -> Unit
) {
    val statuses = OrderStatus.values().toList()

    val filteredOrders = if (selectedStatus == OrderStatus.SEMUA) {
        orders
    } else {
        orders.filter { it.order_status == selectedStatus.dbValue }
    }

    Log.d("OrderListScreen", "Filtered orders ($selectedStatus): $filteredOrders")

    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNavBar(navController) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            TopNavBar(title = "Pesanan", navController = navController)

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(statuses) { status ->
                    TextButton(onClick = { onStatusSelected(status) }) {
                        Text(
                            text = status.displayName,
                            color = if (status == selectedStatus) Color(0xFFFC9824) else Color.Gray,
                            fontWeight = if (status == selectedStatus) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            LazyColumn {
                if (filteredOrders.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tidak ada pesanan")
                        }
                    }
                } else {
                    items(filteredOrders) { order ->
                        OrderCard(
                            order = order,
                            onOrderAccepted = onOrderAccepted,
                            onOrderRejected = onOrderRejected,
                            onOrderDetailed = onOrderDetailed
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

/**
 * Format harga menjadi format rupiah lokal.
 */
fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
    return " Rp ${formatter.format(price)}"
}

/**
 * Komponen kartu untuk satu pesanan, berisi informasi dan aksi.
 */
@Composable
fun OrderCard(
    order: OrderState,
    onOrderAccepted: (OrderState) -> Unit,
    onOrderRejected: (OrderState) -> Unit,
    onOrderDetailed: (OrderState) -> Unit
) {
    var showRejectDialog by remember { mutableStateOf(false) }

    if (showRejectDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showRejectDialog = false },
            title = { Text("Konfirmasi Penolakan", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 24.sp) },
            text = { Text("Yakin ingin tolak pesanan?", color = Color.Black, fontSize = 16.sp) },
            confirmButton = {
                TextButton(onClick = {
                    showRejectDialog = false
                    onOrderRejected(order)
                }) {
                    Text("Ya", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) {
                    Text("Batal", color = Color(0xFFFC9824))
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
            .clickable {
                if (order.order_status == OrderStatus.PROSES.dbValue) onOrderDetailed(order)
            }
            .padding(12.dp)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Pesanan #${order.order_id}", fontWeight = FontWeight.Bold)
            StatusPesanan(order.order_status) // Komponen status
        }

        Text("Dipesan pada ${order.order_time}", fontSize = 14.sp, color = Color.Gray)
        Text("Estimasi selesai ${order.estimation_time} menit", fontSize = 14.sp, color = Color.Gray)

        Divider(modifier = Modifier.padding(vertical = 2.dp))

        order.items.forEach { item ->
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = item.menu_image ?: "",
                    contentDescription = "Gambar Menu",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.menu_name, fontWeight = FontWeight.Bold)
                        Text(formatPrice(item.menu_price), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
//                    Text("Jumlah: ${item.quantity}", fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (order.order_status == OrderStatus.KONFIRMASI.dbValue) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onOrderAccepted(order) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFC9824))
                ) {
                    Text("Terima")
                }
                Button(
                    onClick = { showRejectDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Text("Tolak")
                }
            }
        }
    }
}

//aman
@Composable
fun StatusPesanan(statusDbValue: String) {
    val statusUI = OrderStatus.fromDbValue(statusDbValue)
    val displayName = statusUI?.displayName ?: statusDbValue

    val (color, bgColor) = when (displayName) {
        "Proses" -> Color(0xFFFC9824) to Color(0xFFFFF3E0)
        "Selesai" -> Color(0xFF4CAF50) to Color(0xFFE8F5E9)
        "Batal" -> Color(0xFFF44336) to Color(0xFFFFEBEE)
        "Konfirmasi" -> Color(0xFF1976D2) to Color(0xFFE3F2FD)
        else -> Color.Gray to Color(0xFFE0E0E0)
    }

    Text(
        text = displayName,
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
    val navController = rememberNavController()
    var selectedStatus by remember { mutableStateOf(OrderStatus.SEMUA) }
    val dummyOrders = dummyOrders // dari file OrderDummyData.kt
//    val viewModel = OrderStateViewModel()

    MaterialTheme {
        Surface {
            OrderListScreen(
                navController = navController,
                orders = dummyOrders,
                selectedStatus = selectedStatus,
                onStatusSelected = { selectedStatus = it },
                onOrderAccepted = {},
                onOrderRejected = {},
                onOrderDetailed = {}
            )
        }
    }
}