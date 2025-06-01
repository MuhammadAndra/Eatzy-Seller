package com.example.eatzy_seller.ui.screen.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eatzy_seller.ui.screen.menu.token
import com.example.eatzy_seller.ui.screen.test.TestApiViewModel

@Composable
fun TestApiScreen(

) {
    val vm: TestApiViewModel = viewModel()
    val users by vm.users.collectAsStateWithLifecycle()

//    LaunchedEffect(Unit) { vm.fetchUsersResponse(token = token) }
    Scaffold { innerpadding ->
        Column(modifier = Modifier.padding(innerpadding)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                items(users){ user->
                    Text("${user.name} - ${user.email}")
                }
            }
        }
    }
}
