package com.sachin.shopping.demo.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Int,
    val rating: Double,
    val stock: Int,
    val tagsJson: String,
    val brand: String?,
    val sku: String,
    val weight: Int,
    val dimensionsJson: String,
    val warrantyInformation: String,
    val shippingInformation: String,
    val availabilityStatus: String,
    val reviewsJson: String,
    val returnPolicy: String,
    val minimumOrderQuantity: Int,
    val metaJson: String,
    val thumbnail: String,
    val imagesJson: String
)
