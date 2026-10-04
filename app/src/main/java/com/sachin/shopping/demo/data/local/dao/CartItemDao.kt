package com.sachin.shopping.demo.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
import com.sachin.shopping.demo.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {

    @Query("SELECT * FROM cart_items")
    fun observeCart(): Flow<List<CartItemEntity>>

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items")
    fun observeItemCount(): Flow<Int>

    @Query("SELECT quantity FROM cart_items WHERE productId = :productId")
    suspend fun getQuantity(productId: Int): Int?

    @Upsert
    suspend fun upsert(item: CartItemEntity)

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