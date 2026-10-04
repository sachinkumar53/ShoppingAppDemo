package com.sachin.shopping.demo.data.repository

import com.sachin.shopping.demo.data.local.dao.CartItemDao
import com.sachin.shopping.demo.data.mapper.toModel
import com.sachin.shopping.demo.data.model.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartItemDao: CartItemDao
) {

    val lines = cartItemDao.observeLines()
    val itemCount = cartItemDao.observeItemCount()

    fun observeCart(): Flow<List<CartItem>> {
        return cartItemDao.observeCart().mapLatest { cartItems ->
            cartItems.map { entity -> entity.toModel() }
        }
    }

    fun observeQuantity(productId: Int) = cartItemDao.observeQuantity(productId)

    suspend fun setQuantity(productId: Int, quantity: Int) {
        cartItemDao.setQuantity(productId, quantity)
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