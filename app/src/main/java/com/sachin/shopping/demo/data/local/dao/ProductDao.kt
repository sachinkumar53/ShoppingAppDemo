package com.sachin.shopping.demo.data.local.dao

import androidx.paging.PagingSource
import androidx.room3.Dao
import androidx.room3.DaoReturnTypeConverters
import androidx.room3.Query
import androidx.room3.Upsert
import androidx.room3.paging.PagingSourceDaoReturnTypeConverter
import com.sachin.shopping.demo.data.local.entity.ProductEntity

@Dao
@DaoReturnTypeConverters(PagingSourceDaoReturnTypeConverter::class)
interface ProductDao {

    @Query("SELECT * FROM products")
    fun pagingSource(): PagingSource<Int, ProductEntity>

    @Query("SELECT * FROM products")
    suspend fun getAllEntities(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProduct(productId: Int): ProductEntity?

    @Upsert
    suspend fun upsertAll(products: List<ProductEntity>)

    @Upsert
    suspend fun upsert(product: ProductEntity)

    @Query("DELETE FROM products")
    suspend fun clearAll()
}
