// MainActivity.kt
package com.example.eatzy_seller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.eatzy_seller.navigation.AppNavigation
import com.example.eatzy_seller.ui.theme.EatzySellerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EatzySellerTheme {
                AppNavigation()
            }
        }
    }
}

