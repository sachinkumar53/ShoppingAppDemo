package com.sachin.shopping.demo.data.model

data class ProductListing(
    val id: Int,
    val title: String,
    val price: Double,
    val discountPercentage: Double?,
    val rating: Double,
    val brand: String?,
    val inStock: Boolean,
    val thumbnail: String
)