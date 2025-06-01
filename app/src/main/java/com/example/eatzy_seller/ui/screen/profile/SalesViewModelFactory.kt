//package com.example.eatzy_seller.ui.screen.profile
//
//// ui/screen/profile/SalesViewModelFactory.kt or viewmodel/SalesViewModelFactory.kt
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.eatzy_seller.data.repository.SalesRepository
//
//class SalesViewModelFactory(private val repository: SalesRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(SalesViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return SalesViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}