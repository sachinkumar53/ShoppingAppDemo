package com.sachin.shopping.demo.data.mapper

import com.sachin.shopping.demo.data.model.ProductListing
import com.sachin.shopping.demo.data.remote.dto.Product as ProductDto

fun ProductDto.toProductListing() = ProductListing(
    id = id,
    title = title,
    price = price,
    rating = rating,
    thumbnail = thumbnail,
    brand = brand,
    discountPercentage = discountPercentage,
    inStock = stock > 0
)