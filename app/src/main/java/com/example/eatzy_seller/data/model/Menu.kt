package com.example.eatzy_seller.data.model

import androidx.annotation.DrawableRes

data class Menu(
    val id: Int,
    val title: String,
    val price: String,
    @DrawableRes val imageRes: Int
)
