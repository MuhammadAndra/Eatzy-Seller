// UserViewModel.kt
package com.example.eatzy_seller

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.eatzy_seller.data.repository.AuthState
import com.example.eatzy_seller.data.repository.UserRepository
import com.example.eatzy_seller.data.repository.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    val loginState = repository.loginState
    val userState: StateFlow<UserState> = repository.userState
    val isLoading = mutableStateOf(true)
    val forgotPasswordState: StateFlow<AuthState> = repository.forgotPasswordState
    val resetPasswordState: StateFlow<AuthState> = repository.resetPasswordState

    private val _navigationEvent = Channel<String>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                repository.login(email, password)
                if (loginState.value.success) {
                    Log.d("UserViewModel", "Login successful, navigating to profile")
                    _navigationEvent.send("profile")
                    repository.fetchUserDetails()
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Login error: ${e.message}", e)
                repository.updateLoginState(AuthState(error = e.message ?: "Login failed"))
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            repository.forgotPassword(email)
        }
    }

    fun resetPassword(email: String, otp: String, newPassword: String) {
        viewModelScope.launch {
            repository.resetPassword(email, otp, newPassword)
        }
    }

    fun fetchUserDetails() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                repository.fetchUserDetails()
            } catch (e: Exception) {
                Log.e("UserViewModel", "Fetch user details error: ${e.message}", e)
                repository.updateUserState(UserState(error = "Failed to fetch user details"))
            } finally {
                isLoading.value = false
            }
        }
    }

    fun updateUserName(newName: String) {
        viewModelScope.launch {
            val userId = getUserIdFromToken()
            if (userId != null) {
                repository.updateUser(userId, newName)
            } else {
                Log.e("UserViewModel", "Failed to update name: User ID not found")
                repository.updateUserState(
                    repository.userState.value.copy(error = "User ID not found")
                )
            }
        }
    }

    fun resetState() {
        repository.resetState()
    }

    private suspend fun getUserIdFromToken(): Int? {
        return withContext(Dispatchers.IO) {
            try {
                val token = repository.getToken()
                if (token == null) {
                    Log.e("UserViewModel", "No JWT token found in SharedPreferences")
                    return@withContext null
                }
                Log.d("UserViewModel", "JWT token: ${token.take(20)}")
                val parts = token.split(".")
                if (parts.size != 3) {
                    Log.e("UserViewModel", "Invalid JWT format: ${parts.size} parts")
                    return@withContext null
                }
                val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
                Log.d("UserViewModel", "JWT payload: $payload")
                val idMatch = """"id":(\d+)""".toRegex().find(payload)
                if (idMatch == null) {
                    Log.e("UserViewModel", "No 'id' field found in JWT payload")
                    return@withContext null
                }
                idMatch.groups[1]?.value?.toInt()?.also {
                    Log.d("UserViewModel", "Extracted user ID: $it")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error decoding token: ${e.message}", e)
                null
            }
        }
    }
}

class UserViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(UserRepository(context.applicationContext)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}