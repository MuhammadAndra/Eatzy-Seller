package com.example.eatzy_seller.ui.screen.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RegisterScreen(
    onNavigateToChangePassword: () -> Unit
) {
    Scaffold { innerpadding ->
        Column(modifier = Modifier.padding(innerpadding)) {
            Text(
                "RegisterScreen"
            )
            Button(onClick = onNavigateToChangePassword) { }
        }
    }
}