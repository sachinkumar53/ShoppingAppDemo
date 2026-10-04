package com.sachin.shopping.demo.data.mapper

import com.sachin.shopping.demo.data.model.Dimensions
import com.sachin.shopping.demo.data.model.Meta
import com.sachin.shopping.demo.data.model.Product
import com.sachin.shopping.demo.data.model.ProductListing
import com.sachin.shopping.demo.data.model.Review
import com.sachin.shopping.demo.data.remote.dto.Product as ProductDto
import com.sachin.shopping.demo.data.remote.dto.Dimensions as DimensionsDto
import com.sachin.shopping.demo.data.remote.dto.Review as ReviewDto
import com.sachin.shopping.demo.data.remote.dto.Meta as MetaDto

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

fun ProductDto.toModel(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        tags = tags,
        brand = brand,
        sku = sku,
        weight = weight,
        dimensions = dimensions.toModel(),
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        reviews = reviews.map { it.toModel() },
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        meta = meta.toModel(),
        images = images,
        thumbnail = thumbnail
    )
}

fun DimensionsDto.toModel() = Dimensions(
    width = width,
    height = height,
    depth = depth
)

fun ReviewDto.toModel() = Review(
    rating = rating,
    comment = comment,
    date = date,
    reviewerName = reviewerName,
    reviewerEmail = reviewerEmail
)

fun MetaDto.toModel() = Meta(
    createdAt = createdAt,
    updatedAt = updatedAt,
    barcode = barcode,
    qrCode = qrCode
)