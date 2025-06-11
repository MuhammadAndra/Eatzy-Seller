package com.example.eatzy_seller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
//import com.example.eatzy_seller.data.local.AppDatabase
import com.example.eatzy_seller.data.network.RetrofitClient
import com.example.eatzy_seller.data.repository.OrderRepository
import com.example.eatzy_seller.navigation.AppNavigation
import com.example.eatzy_seller.ui.screen.orderState.OrderStateViewModel
import com.example.eatzy_seller.ui.theme.EatzySellerTheme

//tokennya taro sini, kalo apinya butuh authorisasi
const val token ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwiZW1haWwiOiJleGFtcGxlQGdtYWlsLmNvbSIsInJvbGUiOiJjYW50ZWVuIiwiaWF0IjoxNzQ4NzQ1OTIwLCJleHAiOjE3NjQyOTc5MjB9.8noAM871K4WmbTn_uX4EQ22qeTJOy-JaZV5MhZZ86lc"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val database = AppDatabase.getDatabase(applicationContext)
//        val api = RetrofitClient.orderApi
//        val dao = database.orderDao()
//        val repository = OrderRepository(api, token)
        val viewModel = OrderStateViewModel()

//        enableEdgeToEdge()
        setContent {
            EatzySellerTheme {
//                AppNavigation()
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}
