package com.sachin.shopping.demo.data.repository

import com.sachin.shopping.demo.data.local.dao.CartItemDao
import com.sachin.shopping.demo.data.local.entity.CartItemEntity
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartItemDao: CartItemDao
) {

    fun observeCart() = cartItemDao.observeCart()

    fun observeQuantity(productId: Int) = cartItemDao.observeQuantity(productId)

    suspend fun upsert(productId: Int, quantity: Int = 1) {
        cartItemDao.upsert(CartItemEntity(productId, quantity))
    }

    suspend fun remove(productId: Int) {
        cartItemDao.delete(productId)
    }

    suspend fun clear() {
        cartItemDao.clear()
    }

    suspend fun getQuantity(productId: Int): Int? {
        return cartItemDao.getQuantity(productId)
    }
}