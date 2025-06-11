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
import com.example.eatzy_seller.data.model.AddOn
import com.example.eatzy_seller.data.network.api.OrderApiService
import com.example.eatzy_seller.data.repository.OrderRepository
import com.example.eatzy_seller.navigation.navGraph.Order
import com.example.eatzy_seller.token
import com.example.eatzy_seller.ui.screen.orderState.OrderStateViewModel
import com.example.eatzy_seller.ui.screen.orderState.OrderStatus
import java.text.SimpleDateFormat

//aman
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

//kalau di order detail pakai orderList
@Composable
fun OrderDetailScreen(navController: NavHostController, order: OrderList, onNavigateToOrderFinished: () -> Unit) {
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
                    Text("${order.orderId}", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tanggal Pemesanan", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Text("${com.example.eatzy_seller.ui.screen.orderState.formatOrderTime(order.orderTime)}", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            order.items.forEach {
                OrderItemCard(item = it)
            }
            Divider()

            Spacer(modifier = Modifier.height(8.dp))

            OrderDetailRow(
                label = "Subtotal Pesanan (${order.items.size} item)",
                value = "${formatPrice(order.totalPrice)}"
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
//                    viewModel.updateOrderStatus(order.order_id, "Bearer $token") {
//                    viewModel.updateOrderStatus(order.order_id, "Selesai") {
//                        onNavigateToOrderFinished()
//                    }
                    viewModel.updateOrderStatus(
                        orderId = order.orderId,
                        newStatus = OrderStatus.SELESAI.dbValue,
                        onSuccess = {
                            viewModel.updateSelectedStatus(OrderStatus.SELESAI)
                            navController.navigate(Order.route) {
                                popUpTo(Order.route) { inclusive = false }
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

//aman
fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
    return "Rp ${formatter.format(price)}"
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

//aman
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

//aman
@Composable
fun OrderItemCard(item: OrderItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (item.menuImage != null) {
            AsyncImage(
                model = item.menuImage,
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

        Log.d("IMAGE_URL", "Image URL: ${item.menuImage}")

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
                Text(item.menuName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("${formatPrice(item.menuPrice)}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
//            Text("Jumlah: ${item.quantity}", fontSize = 14.sp)
            if (item.addOns.isNotEmpty()) {
                Text(
                    text = "${item.addOns.joinToString(", ") { it.name }}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
            Text("Catatan: ${item.itemDetails}", fontSize = 14.sp, color = Color.Gray)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
fun PreviewOrderDetailScreen() {
    val navHostController = rememberNavController()

    val order = OrderList(
        orderId = 1,
        orderTime = "13/03/2025",
        totalPrice = 25000.0,
        items = listOf(
            OrderItem(
                menuId = 1,
                menuName = "Ayam Bakar",
                itemDetails = "nasinya dikit aja",
                menuImage = "https://www.unileverfoodsolutions.co.id/dam/global-ufs/mcos/SEA/calcmenu/recipes/ID-recipes/chicken-&-other-poultry-dishes/crispy-fried-chicken/Ayam%20Goreng%20Krispy1260x700.jpg",
                menuPrice = 12000.0,
                addOns = listOf(
                    AddOn(id = 1, name = "Sambal Bawang"),
                    AddOn(id = 2, name = "Tempe Goreng")
                )
            )
        )
    )

    MaterialTheme {
        OrderDetailScreen(
            navController = navHostController,
//            viewModel = OrderStateViewModel(repository = OrderRepository(api = )),
            order = order,
            onNavigateToOrderFinished = {}
        )
    }
}