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
import com.example.eatzy_seller.navigation.AppNavigation
import com.example.eatzy_seller.ui.theme.EatzySellerTheme

//tokennya taro sini, kalo apinya butuh authorisasi
const val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NCwiZW1haWwiOiJidWtyaXNAZXhhbXBsZS5jb20iLCJyb2xlIjoiY2FudGVlbiIsImlhdCI6MTc0ODU2NDc4MSwiZXhwIjoxNzY0MTE2NzgxfQ.UVXsenWc8EouRLgKZ9O8kpm_Ae2RP89jomX5-FWLYOQ"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EatzySellerTheme {
                AppNavigation()
            }
        }
    }
}
