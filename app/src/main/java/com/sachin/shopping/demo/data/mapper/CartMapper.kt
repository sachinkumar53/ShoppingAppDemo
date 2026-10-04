package com.sachin.shopping.demo.data.mapper

import com.sachin.shopping.demo.data.local.entity.CartItemEntity
import com.sachin.shopping.demo.data.model.CartItem

fun CartItemEntity.toModel(): CartItem {
    return CartItem(
        productId = productId,
        quantity = quantity
    )
}