// data/network/api/SalesApi.kt
package com.example.eatzy_seller.data.network.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName

interface SalesApi {
    @GET("sales/monthly")
    suspend fun getMonthlySales(
        @Header("Authorization") authHeader: String,
        @Query("canteenId") canteenId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Response<SalesResponse>
}

data class SalesResponse(
    @SerializedName("totalSales") val totalSales: String,
    val error: String? = null
)