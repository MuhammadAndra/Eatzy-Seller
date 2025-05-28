package com.example.eatzy_seller.ui.screen.orderDetail

import android.icu.text.NumberFormat
import android.util.Log
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.eatzy_seller.data.model.OrderList
import com.example.eatzy_seller.data.model.OrderItem
import com.bumptech.glide.integration.compose.GlideImage
import java.util.Locale
import coil.compose.AsyncImage
import com.example.eatzy_seller.ui.components.BottomNavBar
import androidx.navigation.compose.rememberNavController
import com.example.eatzy_seller.navigation.navGraph.Order
import com.example.eatzy_seller.token
import com.example.eatzy_seller.ui.screen.orderState.OrderStateViewModel

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

        Spacer(modifier = Modifier.size(48.dp)) // Space untuk menyamakan kanan
    }
}

@Composable
fun OrderDetailScreen(navController: NavHostController, order: OrderList, onNavigateToOrderFinished : () -> Unit) {
    val viewModel: OrderStateViewModel = viewModel()

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BottomNavBar(navController = navController)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = innerPadding.calculateBottomPadding())
                .background(Color.White)
        ) {
            TopNavBar(title = "Detail Pesanan", navController = navController)

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("No. Pesanan", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Text("${order.order_id}", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tanggal Pemesanan", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Text("${order.order_time}", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            order.items.forEach {
                OrderItemCard(item = it)
            }
            Divider()

            Spacer(modifier = Modifier.height(8.dp))

            val totalHarga = order.items.sumOf { it.quantity * it.menu_price }
            OrderDetailRow(
                label = "Subtotal Pesanan (${order.items.size} item)",
                value = "${formatPrice(totalHarga)}"
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
//                    viewModel.updateOrderStatus(order.order_id, "Bearer $token") {
//                    viewModel.updateOrderStatus(order.order_id, "Selesai") {
//                        onNavigateToOrderFinished()
//                    }
                    viewModel.updateOrderStatus(
                        token = token,
                        orderId = order.order_id,
                        newStatus = "Selesai",
                        onSuccess = {
                            viewModel.updateSelectedStatus("Selesai", token)
                            navController.navigate(Order.route) {
                                popUpTo(Order.route) { inclusive = true }
                            }
                            // aksi jika berhasil, misal tampilkan snackbar atau navigasi
                        },
                        onError = { errorMessage ->
                            // aksi jika gagal, misal tampilkan Toast atau Log
                            Log.e("UpdateOrder", "Gagal update: $errorMessage")
                        }
                    )

                },
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
        if (item.menu_image != null) {
            AsyncImage(
                model = item.menu_image,
                contentDescription = "Menu Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            // Bisa tampilkan Box kosong atau ikon default
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "No Image"
                )
            }
        }

//        AsyncImage(
//            model = item.menu_image,
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(80.dp)
//                .clip(RoundedCornerShape(8.dp))
//        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(item.menu_name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("${formatPrice(item.menu_price)}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Text("Jumlah: ${item.quantity}", fontSize = 14.sp)
            Text(item.add_on, fontSize = 14.sp)
            Text("Catatan: ${item.item_details}", fontSize = 14.sp, color = Color.Gray)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
fun PreviewOrderDetailScreen() {
    val navHostController = rememberNavController()
    val order = OrderList(
        order_id = 1,
        order_time = "13/03/2025",
        total_price = 25.000,
        items = listOf(
            OrderItem(1, "Ayam Bakar", "nasinya dikit aja", "https://img.freepik.com/premium-photo/ayam-goreng-serundeng-fried-chicken-sprinkled-with-grated-coconut-with-curry-spices-serundeng_431906-4528.jpg?w=1480", 12000.0, 1, "sambal bawang")
//            OrderItem("Ayam Goreng", 1, "sambalnya banyakin", "sambal bawang, makan di tempat", 12000.0, "https://img.freepik.com/premium-photo/ayam-goreng-serundeng-fried-chicken-sprinkled-with-grated-coconut-with-curry-spices-serundeng_431906-4528.jpg?w=1480"),
//            OrderItem("Jamur Crispy", 1, "nasinya dikit aja","sambal tomat, bungkus",13000.0, "https://lh3.googleusercontent.com/-1tk-T-5FB6I/W-zojV95WHI/AAAAAAAAACQ/VPjFLsTS4cIZ61q_IhQt_n0i7H_Nb9xtwCHMYCw/s1600/20181113_094220.png"),
//            OrderItem("Ayam Ungkep", 1, "sambalnya banyakin","sambal bawang, bungkus",13000.0, "https://whattocooktoday.com/wp-content/uploads/2020/03/ayam-ungkep-7-585x878.jpg")
        )
    )
    MaterialTheme {
        Surface {
            OrderDetailScreen(navController = navHostController, order = order, onNavigateToOrderFinished = {})
        }
    }
}
