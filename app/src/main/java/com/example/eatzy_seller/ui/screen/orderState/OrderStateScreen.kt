package com.example.eatzy_seller.ui.screen.orderState

import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.eatzy_seller.data.dummyOrders
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.eatzy_seller.R
import com.example.eatzy_seller.data.model.OrderItem
import com.example.eatzy_seller.data.model.OrderState
import com.example.eatzy_seller.token
import com.example.eatzy_seller.ui.components.BottomNavBar
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

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

@Composable
fun OrderStateScreen(
    navController: NavHostController,
    viewModel: OrderStateViewModel, // Get the ViewModel instance
) {
    // Collect the state flows from the ViewModel
    val orders by viewModel.orders.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Log the filtered orders
    Log.d("OrderListScreen", "Filtered orders ($selectedStatus): $orders")

    //isi scaffold ini untuk bottombar sama orderstate di bawah topbar
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
                items(OrderStatus.values().toList()) { status ->
                    TextButton(onClick = { viewModel.updateSelectedStatus(status) }) {
                        Text(
                            text = status.displayName,
                            color = if (status == selectedStatus) Color(0xFFFC9824) else Color.Gray,
                            fontWeight = if (status == selectedStatus) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            //tes apakah pesanan bisa masuk, ini nnt dihapus
//            if (isLoading) {
//                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
//            } else if (error != null) {
//                Text("Error: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
//            } else {
//                Text("Total orders: ${orders.size}", modifier = Modifier.padding(8.dp))

            LazyColumn {
                if (orders.isEmpty()) {
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
                    items(orders) { order ->
                        OrderCard(
                            order = order,
                            onOrderAccepted = {
                                viewModel.acceptOrder(order)
                                Log.d("CHECK ACCEPT","$error")
                            },
                            onOrderRejected = { viewModel.rejectOrder(order)
                                Log.d("CHECK REJECT","$error")
                            },
                            onOrderDetailed = {
                                navController.navigate("orderDetail/${order.orderId}")
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
    return " Rp ${formatter.format(price)}"
}

fun formatOrderTime(raw: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        formatter.format(parser.parse(raw)!!)
    } catch (e: Exception) {
        raw
    }
}


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
            title = {
                Text(
                    "Konfirmasi Penolakan",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            },
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
                if (order.orderStatus == OrderStatus.PROSES.dbValue) onOrderDetailed(order)
            }
            .padding(12.dp)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Pesanan #${order.orderId}", fontWeight = FontWeight.Bold)
            StatusPesanan(order.orderStatus) // Komponen status
        }

        Text(
            "Dipesan pada ${formatOrderTime(order.orderTime)}",
            fontSize = 14.sp,
            color = Color.Gray
        )

        //ini hanya tampil klo pilih pesan untuk nanti
        if (!order.scheduleTime.isNullOrEmpty()) {
            Text(
                text = "Dipesan untuk ${formatOrderTime(order.scheduleTime)}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Divider(modifier = Modifier.padding(vertical = 2.dp))

        order.items.forEachIndexed { index, item ->
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!item.menuImage.isNullOrBlank()) {
                    AsyncImage(
                        model = item.menuImage,
                        contentDescription = "Gambar Menu",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    )
                } else {
                    // Fallback gambar lokal atau placeholder
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Gambar Default"
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.menuName, fontWeight = FontWeight.Bold)
                        Text(
                            formatPrice(item.menuPrice),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
//                    Text(text = "Jumlah: ", fontSize = 4.dp)
                    Text("Jumlah: ", fontSize = 14.sp)
                    if (item.addOns.isNotEmpty()) {
                        Text(
                            text = "${item.addOns.joinToString(", ") { it.name}}",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                    if (!item.itemDetails.isNullOrBlank()) {
                        Text("Catatan: ${item.itemDetails}", fontSize = 14.sp, color = Color.Gray)
                    }

                }
            }
            if (index == order.items.lastIndex) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text("Total: ${formatPrice(order.totalPrice)}", fontWeight = FontWeight.Bold)
                }
            }
        }

        //pelajari ini
        if (order.orderStatus == OrderStatus.KONFIRMASI.dbValue) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onOrderAccepted(order) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFC9824))
                ) {
                    Text("Terima", color = Color.White)
                }
                Spacer(modifier = Modifier.size(15.dp))
                Button(
                    onClick = { showRejectDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF455E84))
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
            OrderStateScreen(
                navController = navController,
                viewModel = viewModel()
            )
        }
    }
}