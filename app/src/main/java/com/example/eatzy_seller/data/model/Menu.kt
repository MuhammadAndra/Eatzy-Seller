package com.example.eatzy_seller.data.model

data class Menu(
    val idMenu : Int,
    val namaMenu: String,
    //val idKategori: Int,
    val price: Double,
    //val preparationTime: String,
    val imageRes: String,
    val visibleMenu: Boolean
)
