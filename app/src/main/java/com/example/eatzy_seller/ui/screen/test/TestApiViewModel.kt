package com.example.eatzy_seller.ui.screen.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatzy_seller.data.model.User
import com.example.eatzy_seller.data.network.RetrofitClient
import com.example.eatzy_seller.data.repository.TestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestApiViewModel: ViewModel(){
    //contoh kalo mau ngambil user
    private val repository = TestRepository()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    suspend fun fetchUsersResponse(token: String) {
        val response = repository.getUsersResponse(token = token)
        if (response.isSuccessful) {
            _users.value = response.body() ?: emptyList()
        }
    }

//    ini fungsi ambil data dari pak aryo
//    fun fetchUsers(token: String) {
//        repository.getUsers(
//            token = token,
//            onSuccess = { _users.value = it },
//            onError = { throwable -> }
//        )
//    }
//    fun fetchUsers(){
//        viewModelScope.launch {
//            RetrofitClient.testApi.getUsers().enqueue(
//                object : Callback<List<User>> {
//                    override fun onResponse(
//                        call: Call<List<User>>,
//                        response: Response<List<User>>
//                    ) {
//                        //koneksi request http berhasil dijalankan
//                        if (response.isSuccessful) {
//                            _users.value = response.body() ?: emptyList()
//                        }
//                    }
//
//                    override fun onFailure(
//                        call: Call<List<User>>,
//                        t: Throwable?
//                    ) {
//                        //kalo requestnya gagal kesini
//                    }
//
//                }
//            )
//        }
//    }
}