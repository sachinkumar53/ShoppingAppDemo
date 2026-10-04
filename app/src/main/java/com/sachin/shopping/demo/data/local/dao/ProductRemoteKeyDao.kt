package com.sachin.shopping.demo.data.local.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import com.sachin.shopping.demo.data.local.entity.ProductRemoteKey

@Dao
interface ProductRemoteKeyDao {
    @Query("SELECT * FROM product_remote_keys WHERE productId = :id")
    suspend fun get(id: Int): ProductRemoteKey?

    @Upsert
    suspend fun upsertAll(keys: List<ProductRemoteKey>)

    @Query("SELECT MAX(createdAt) FROM product_remote_keys")
    suspend fun lastCreatedAt(): Long?

    @Query("DELETE FROM product_remote_keys")
    suspend fun clear()
}