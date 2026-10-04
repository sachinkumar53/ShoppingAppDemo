package com.sachin.shopping.demo.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "product_remote_keys")
data class ProductRemoteKey(
    @PrimaryKey val productId: Int,
    val nextSkip: Int?,              // null = last page
    val createdAt: Long = System.currentTimeMillis()
)