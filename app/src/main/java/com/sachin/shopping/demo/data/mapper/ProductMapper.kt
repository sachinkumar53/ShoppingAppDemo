package com.sachin.shopping.demo.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sachin.shopping.demo.data.local.entity.ProductEntity
import com.sachin.shopping.demo.data.model.Dimensions
import com.sachin.shopping.demo.data.model.Meta
import com.sachin.shopping.demo.data.model.Product
import com.sachin.shopping.demo.data.model.ProductListing
import com.sachin.shopping.demo.data.model.Review
import kotlin.math.roundToInt
import com.sachin.shopping.demo.data.remote.dto.Dimensions as DimensionsDto
import com.sachin.shopping.demo.data.remote.dto.Meta as MetaDto
import com.sachin.shopping.demo.data.remote.dto.Product as ProductDto
import com.sachin.shopping.demo.data.remote.dto.Review as ReviewDto

private val gson = Gson()

fun ProductDto.toProductListing() = ProductListing(
    id = id,
    title = title,
    price = price,
    rating = rating,
    thumbnail = thumbnail,
    brand = brand,
    discountPercentage = discountPercentage.roundToInt(),
    inStock = stock > 0
)

fun ProductDto.toModel(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage.roundToInt(),
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

fun ProductDto.toEntity() = ProductEntity(
    id = id,
    title = title,
    description = description,
    category = category,
    price = price,
    discountPercentage = discountPercentage.roundToInt(),
    rating = rating,
    stock = stock,
    tagsJson = gson.toJson(tags),
    brand = brand,
    sku = sku,
    weight = weight,
    dimensionsJson = gson.toJson(dimensions.toModel()),
    warrantyInformation = warrantyInformation,
    shippingInformation = shippingInformation,
    availabilityStatus = availabilityStatus,
    reviewsJson = gson.toJson(reviews.map { it.toModel() }),
    returnPolicy = returnPolicy,
    minimumOrderQuantity = minimumOrderQuantity,
    metaJson = gson.toJson(meta.toModel()),
    thumbnail = thumbnail,
    imagesJson = gson.toJson(images)
)

fun Product.toEntity() = ProductEntity(
    id = id,
    title = title,
    description = description,
    category = category,
    price = price,
    discountPercentage = discountPercentage,
    rating = rating,
    stock = stock,
    tagsJson = gson.toJson(tags),
    brand = brand,
    sku = sku,
    weight = weight,
    dimensionsJson = gson.toJson(dimensions),
    warrantyInformation = warrantyInformation,
    shippingInformation = shippingInformation,
    availabilityStatus = availabilityStatus,
    reviewsJson = gson.toJson(reviews),
    returnPolicy = returnPolicy,
    minimumOrderQuantity = minimumOrderQuantity,
    metaJson = gson.toJson(meta),
    thumbnail = thumbnail,
    imagesJson = gson.toJson(images)
)

fun ProductEntity.toModel(): Product {
    val tagListType = object : TypeToken<List<String>>() {}.type
    val reviewListType = object : TypeToken<List<Review>>() {}.type
    val imageListType = object : TypeToken<List<String>>() {}.type

    return Product(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        tags = gson.fromJson(tagsJson, tagListType) ?: emptyList(),
        brand = brand,
        sku = sku,
        weight = weight,
        dimensions = gson.fromJson(dimensionsJson, Dimensions::class.java),
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        reviews = gson.fromJson(reviewsJson, reviewListType) ?: emptyList(),
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        meta = gson.fromJson(metaJson, Meta::class.java),
        thumbnail = thumbnail,
        images = gson.fromJson(imagesJson, imageListType) ?: emptyList()
    )
}

fun ProductEntity.toProductListing() = ProductListing(
    id = id,
    title = title,
    price = price,
    rating = rating,
    thumbnail = thumbnail,
    brand = brand,
    discountPercentage = discountPercentage,
    inStock = stock > 0
)

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
