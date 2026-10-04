package com.sachin.shopping.demo.data.local.entity

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "cart_items",
    indices = [Index(value = ["productId", "quantity"], unique = true)]
)
data class CartItemEntity(
    @PrimaryKey
    val productId: Int,
    val quantity: Int
)