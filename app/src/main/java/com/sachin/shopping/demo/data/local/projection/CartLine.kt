package com.sachin.shopping.demo.data.local.projection

data class CartLine(
    val productId: Int,
    val quantity: Int,
    val title: String,
    val brand: String?,
    val thumbnail: String,
    val price: Double,                 // list price
    val discountPercentage: Double,
    val stock: Int,
    val minimumOrderQuantity: Int
) {
    val unitPrice: Double get() = price * (1 - discountPercentage / 100)
    val lineTotal: Double get() = unitPrice * quantity
}