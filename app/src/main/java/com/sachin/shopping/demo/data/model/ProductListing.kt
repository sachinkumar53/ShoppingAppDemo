package com.sachin.shopping.demo.data.model

data class ProductListing(
    val id: Int,
    val title: String,
    val price: Double,
    val discountPercentage: Int,
    val rating: Double,
    val brand: String?,
    val inStock: Boolean,
    val thumbnail: String
) {
    val discountedPrice = discountPercentage.takeIf { it > 0 }?.let { price * (1 - it / 100.0) }
}