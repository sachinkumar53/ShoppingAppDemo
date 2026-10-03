package com.sachin.shopping.demo.data.remote.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Meta(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("barcode")
    val barcode: String,
    @SerializedName("qrCode")
    val qrCode: String
)