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
const val token ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiZW1haWwiOiJhbmRyYWxtYUBleGFtcGxlLmNvbSIsInJvbGUiOiJjYW50ZWVuIiwiaWF0IjoxNzQ3MTkxOTA2LCJleHAiOjE3NDcxOTU1MDZ9.uPqT6DXBYM6JQEhLQ12Pbaf9SxxKxxPNYpu6KKypZMA"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val api = RetrofitClient.orderApi
//        val dao = database.orderDao()
        val repository = OrderRepository(api)
        val viewModel = OrderStateViewModel(repository)

        enableEdgeToEdge()
        setContent {
            EatzySellerTheme {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}
