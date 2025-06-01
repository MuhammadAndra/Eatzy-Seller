// data/repository/SalesRepository.kt
package com.example.eatzy_seller.data.repository

import android.content.Context
import com.example.eatzy_seller.data.network.api.SalesApi
import com.example.eatzy_seller.data.network.api.SalesResponse

class SalesRepository(
    private val salesApi: SalesApi,
    private val context: Context
) {
    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    suspend fun getMonthlySales(canteenId: Int, month: Int, year: Int): Result<Double> {
        return try {
            val token = sharedPreferences.getString("TOKEN_KEY", null)
                ?: return Result.failure(Exception("No token found"))
            val response = salesApi.getMonthlySales("Bearer $token", canteenId, month, year)
            if (response.isSuccessful && response.body() != null) {
                val salesResponse = response.body()!!
                if (salesResponse.error == null) {
                    val totalSales = salesResponse.totalSales.toDoubleOrNull()
                        ?: return Result.failure(Exception("Invalid totalSales format"))
                    Result.success(totalSales)
                } else {
                    Result.failure(Exception(salesResponse.error))
                }
            } else {
                Result.failure(Exception("Failed to fetch sales: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}