package com.example.eatzy_seller.data.model

import com.google.gson.annotations.SerializedName

data class AddOn(
    @SerializedName("addon_id")
    val AddOnId : Int,

    @SerializedName("addon_name")
    val AddOnName: String,

    @SerializedName("addon_price")
    val AddOnPrice: Double,

    @SerializedName("addon_is_available")
    val AddOnAvailable : Boolean
)

data class UpdateAddonRequest(
    @SerializedName("addon_name")
    val AddOnName: String,

    @SerializedName("addon_price")
    val AddOnPrice: Double
)