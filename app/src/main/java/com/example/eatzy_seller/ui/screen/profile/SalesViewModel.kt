// ui/screen/profile/SalesViewModel.kt
package com.example.eatzy_seller.ui.screen.profile

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.eatzy_seller.data.network.RetrofitClient
import com.example.eatzy_seller.data.repository.SalesRepository
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class SalesViewModel(
    private val repository: SalesRepository
) : ViewModel() {
    val income = mutableStateOf("Rp 0.00")
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    fun fetchMonthlySales(canteenId: Int, month: Int, year: Int) {
        isLoading.value = true
        error.value = null

        viewModelScope.launch {
            val result = repository.getMonthlySales(canteenId, month, year)
            if (result.isSuccess) {
                val amount = result.getOrNull() ?: 0.0
                val df = DecimalFormat("#,###.##")
                income.value = "Rp ${df.format(amount)}"
            } else {
                error.value = result.exceptionOrNull()?.message ?: "Failed to load sales data"
            }
            isLoading.value = false
        }
    }
}

class SalesViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SalesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SalesViewModel(SalesRepository(RetrofitClient.salesApi, context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}