package com.sachin.shopping.demo.data.model

import kotlin.math.roundToInt

data class ProductListItem(
    val id: Int,
    val title: String,
    val brand: String?,
    val thumbnail: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double
) {
    val unitPrice: Double
        get() = (price * (1 - discountPercentage / 100) * 100).roundToInt() / 100.0
}