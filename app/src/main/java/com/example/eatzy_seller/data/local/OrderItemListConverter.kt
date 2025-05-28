package com.example.eatzy_seller.data.local

import androidx.room.TypeConverter
import com.example.eatzy_seller.data.model.OrderItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class OrderItemListConverter {

    @TypeConverter
    fun fromList(items: List<OrderItem>): String {
//        return Gson().toJson(items)
        return Json.encodeToString(items)
    }

    @TypeConverter
    fun toList(data: String): List<OrderItem> {
//        val type = object : TypeToken<List<OrderItem>>() {}.type
//        return Gson().fromJson(json, type)
        return Json.decodeFromString(data)
    }
}
