package com.sachin.shopping.demo.data.repository

import com.sachin.shopping.demo.data.local.dao.CartItemDao
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartItemDao: CartItemDao
) {

    val lines = cartItemDao.observeLines()

    fun observeQuantity(productId: Int) = cartItemDao.observeQuantity(productId)

    suspend fun setQuantity(productId: Int, quantity: Int) {
        cartItemDao.setQuantity(productId, quantity)
    }

    suspend fun clear() {
        cartItemDao.clear()
    }

    suspend fun getQuantity(productId: Int): Int? {
        return cartItemDao.getQuantity(productId)
    }
}