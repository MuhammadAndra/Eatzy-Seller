// data/network/api/ApiService.kt
package com.example.eatzy_seller.data.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String?, val error: String?)
data class UserResponse(val id: Int, val name: String, val email: String, val role: String, val error: String?)
data class ForgottenPasswordRequest(val email: String)
data class ForgottenPasswordResponse(val message: String?, val error: String?)
data class ResetPasswordRequest(val email: String, val otp: String, val password: String)
data class ResetPasswordResponse(val message: String?, val error: String?)
data class UpdateUserRequest(val name: String)

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("auth/user")
    suspend fun getUser(@Header("Authorization") token: String): Response<UserResponse>

    @PUT("users/{id}") // Updated endpoint to /users/{id}
    suspend fun updateUser(
        @Path("id") userId: Int,
        @Header("Authorization") token: String,
        @Body request: UpdateUserRequest
    ): Response<UserResponse>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgottenPasswordRequest): Response<ForgottenPasswordResponse>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>
}