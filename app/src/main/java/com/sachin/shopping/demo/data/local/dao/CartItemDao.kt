package com.sachin.shopping.demo.data.local.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
import com.sachin.shopping.demo.data.local.entity.CartItemEntity
import com.sachin.shopping.demo.data.local.projection.CartLine
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {

    @Query("""
        SELECT c.productId, c.quantity, p.title, p.brand, p.thumbnail,
               p.price, p.discountPercentage, p.stock, p.minimumOrderQuantity
        FROM cart_items c
        INNER JOIN products p ON p.id = c.productId
        ORDER BY c.addedAt DESC
    """)
    fun observeLines(): Flow<List<CartLine>>

    @Query("SELECT * FROM cart_items")
    fun observeCart(): Flow<List<CartItemEntity>>

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items")
    fun observeItemCount(): Flow<Int>

    @Query("SELECT quantity FROM cart_items WHERE productId = :productId")
    suspend fun getQuantity(productId: Int): Int?

    @Upsert
    suspend fun upsert(item: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE productId = :productId")
    suspend fun setQuantity(productId: Int, quantity: Int)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun delete(productId: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clear()

    @Transaction
    suspend fun add(productId: Int, qty: Int = 1, maxQty: Int = 10) {
        val current = getQuantity(productId) ?: 0
        upsert(CartItemEntity(productId, (current + qty).coerceAtMost(maxQty)))
    }

    @Query("SELECT quantity FROM cart_items WHERE productId = :productId")
    fun observeQuantity(productId: Int): Flow<Int?>
}