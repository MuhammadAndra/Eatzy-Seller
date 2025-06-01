// ui/screen/login/LoginScreen.kt
package com.example.eatzy_seller.ui.screen.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eatzy_seller.R
import com.example.eatzy_seller.UserViewModel
import com.example.eatzy_seller.UserViewModelFactory
import com.example.eatzy_seller.ui.components.PasswordTextField
import com.example.eatzy_seller.ui.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: UserViewModel = viewModel(factory = UserViewModelFactory(LocalContext.current))
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsState()

    // Handle navigation events
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { route ->
            if (route == "profile") {
                onNavigateToProfile()
            }
        }
    }

    // Handle errors
    LaunchedEffect(loginState.error) {
        if (loginState.error != null) {
            Toast.makeText(context, loginState.error, Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_login),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Masuk Kantin",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PasswordTextField(
                        value = password,
                        onValueChange = { password = it },
                        passwordVisible = passwordVisible,
                        onVisibilityToggle = { passwordVisible = !passwordVisible }
                        , label = { Text("Password") }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Lupa Password?",
                        color = Color(0xFFF59A2F),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { viewModel.forgotPassword(email) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Masuk",
                    onClick = { viewModel.login(email, password) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Text(
                        text = "Belum punya akun kantin?",
                        color = Color(0xFFACBED8),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Daftar",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF59A2F),
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { onNavigateToRegister() }
                    )
                }
            }
        }
    }
}