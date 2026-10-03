package com.sachin.shopping.demo.data.remote.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Review(
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("reviewerName")
    val reviewerName: String,
    @SerializedName("reviewerEmail")
    val reviewerEmail: String
)