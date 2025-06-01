// data/repository/UserRepository.kt
package com.example.eatzy_seller.data.repository

import android.content.Context
import android.util.Log
import com.example.eatzy_seller.data.network.RetrofitClient
import com.example.eatzy_seller.data.network.RetrofitClient.apiService
import com.example.eatzy_seller.data.network.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class AuthState(
    val success: Boolean = false,
    val error: String? = null,
    val token: String? = null
)

data class UserState(
    val isLoading: Boolean = false,
    val id: Int? = null,
    val name: String? = null,
    val email: String? = null,
    val role: String? = null,
    val error: String? = null
)

class UserRepository(private val context: Context) {
//    private val apiService = try {
//        RetrofitClient.instance
//    } catch (e: Exception) {
//        Log.e("UserRepository", "Failed to initialize Retrofit: ${e.message}", e)
//        throw e
//    }

    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    private val _loginState = MutableStateFlow(AuthState())
    val loginState: StateFlow<AuthState> get() = _loginState
    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> get() = _userState
    private val _forgotPasswordState = MutableStateFlow(AuthState())
    val forgotPasswordState: StateFlow<AuthState> get() = _forgotPasswordState
    private val _resetPasswordState = MutableStateFlow(AuthState())
    val resetPasswordState: StateFlow<AuthState> get() = _resetPasswordState

    fun updateLoginState(state: AuthState) {
        _loginState.value = state
    }

    fun updateUserState(state: UserState) {
        _userState.value = state
    }

    suspend fun login(email: String, password: String) {
        try {
            _loginState.value = AuthState()
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body()?.token != null) {
                val token = response.body()!!.token!!
                sharedPreferences.edit()
                    .putString("TOKEN_KEY", token)
                    .putString("email", email)
                    .apply()
                _loginState.value = AuthState(success = true, token = token)
            } else {
                val error = response.body()?.error ?: "Login failed"
                _loginState.value = AuthState(error = error)
            }
        } catch (e: Exception) {
            _loginState.value = AuthState(error = e.message ?: "Network error")
        }
    }

    suspend fun fetchUserDetails() {
        try {
            _userState.value = UserState(isLoading = true)
            val token = sharedPreferences.getString("TOKEN_KEY", null)
            if (token == null) {
                Log.e("UserRepository", "No token found in SharedPreferences")
                _userState.value = UserState(isLoading = false, error = "No token found")
                return
            }
            Log.d("UserRepository", "Fetching user with token: ${token.take(20)}...")
            val response = apiService.getUser("Bearer $token")
            Log.d("UserRepository", "GET /auth/user response: ${response.code()} ${response.message()}")
            if (response.isSuccessful && response.body()?.name != null) {
                _userState.value = UserState(
                    isLoading = false,
                    id = response.body()!!.id,
                    name = response.body()!!.name,
                    email = response.body()!!.email,
                    role = response.body()!!.role
                )
                Log.d("UserRepository", "User details: id=${response.body()!!.id}, name=${response.body()!!.name}, email=${response.body()!!.email}, role=${response.body()!!.role}")
            } else {
                val error = response.body()?.error ?: "Failed to fetch user details"
                Log.e("UserRepository", "Fetch user error: $error")
                _userState.value = UserState(isLoading = false, error = error)
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Fetch user error: ${e.message}", e)
            _userState.value = UserState(isLoading = false, error = e.message ?: "Network error")
        }
    }


    suspend fun updateUser(userId: Int, name: String) {
        try {
            _userState.value = _userState.value.copy(isLoading = true)
            val token = sharedPreferences.getString("TOKEN_KEY", null)
            if (token == null) {
                Log.e("UserRepository", "No token found in SharedPreferences")
                _userState.value = _userState.value.copy(isLoading = false, error = "No token found")
                return
            }
            val response = apiService.updateUser(userId, "Bearer $token", UpdateUserRequest(name))
            if (response.isSuccessful && response.body() != null) {
                _userState.value = _userState.value.copy(
                    isLoading = false,
                    name = response.body()!!.name
                )
                Log.d("UserRepository", "User updated: name=${response.body()!!.name}")
            } else {
                val error = response.body()?.error ?: "Failed to update user: ${response.code()}"
                Log.e("UserRepository", "Update user error: $error")
                _userState.value = _userState.value.copy(isLoading = false, error = error)
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Update user error: ${e.message}", e)
            _userState.value = _userState.value.copy(isLoading = false, error = e.message ?: "Network error")
        }
    }

    suspend fun forgotPassword(email: String) {
        try {
            val response = apiService.forgotPassword(ForgottenPasswordRequest(email))
            if (response.isSuccessful && response.body()?.message != null) {
                _forgotPasswordState.value = AuthState(success = true)
            } else {
                _forgotPasswordState.value = AuthState(error = response.body()?.error ?: "Failed to send reset OTP")
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Forgot password error: ${e.message}", e)
            _forgotPasswordState.value = AuthState(error = e.message ?: "Network error")
        }
    }

    suspend fun resetPassword(email: String, otp: String, newPassword: String) {
        try {
            val response = apiService.resetPassword(ResetPasswordRequest(email, otp, newPassword))
            if (response.isSuccessful && response.body()?.message != null) {
                _resetPasswordState.value = AuthState(success = true)
            } else {
                _resetPasswordState.value = AuthState(error = response.body()?.error ?: "Failed to reset password")
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Reset password error: ${e.message}", e)
            _resetPasswordState.value = AuthState(error = e.message ?: "Network error")
        }
    }

    fun resetState() {
        _loginState.value = AuthState()
        _userState.value = UserState()
        _forgotPasswordState.value = AuthState()
        _resetPasswordState.value = AuthState()
        sharedPreferences.edit().clear().apply()
    }

    fun getToken(): String? {
        val token = sharedPreferences.getString("TOKEN_KEY", null)
        Log.d("UserRepository", "Retrieved token: ${token?.take(20)}")
        return token
    }

    fun getEmail(): String? {
        return sharedPreferences.getString("email", null)
    }
}