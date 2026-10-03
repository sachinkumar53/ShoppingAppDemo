package com.sachin.shopping.demo.data.remote.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Dimensions(
    @SerializedName("width")
    val width: Double,
    @SerializedName("height")
    val height: Double,
    @SerializedName("depth")
    val depth: Double
)