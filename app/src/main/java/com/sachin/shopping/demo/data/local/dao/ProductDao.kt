package com.sachin.shopping.demo.data.local.dao

import androidx.paging.PagingSource
import androidx.room3.Dao
import androidx.room3.DaoReturnTypeConverters
import androidx.room3.Query
import androidx.room3.Upsert
import androidx.room3.paging.PagingSourceDaoReturnTypeConverter
import com.sachin.shopping.demo.data.local.entity.ProductEntity
import com.sachin.shopping.demo.data.model.ProductListItem
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM products WHERE id = :id")
    fun observe(id: Int): Flow<ProductEntity?>

    @Query(
        """
    SELECT id, title, brand, thumbnail, price, discountPercentage, rating
    FROM products
    WHERE title LIKE '%' || :query || '%' ESCAPE '\'
       OR brand LIKE '%' || :query || '%' ESCAPE '\'
       OR category LIKE '%' || :query || '%' ESCAPE '\'
    ORDER BY rating DESC
    LIMIT 50
    """
    )
    fun search(query: String): Flow<List<ProductListItem>>

    @Query("DELETE FROM products")
    suspend fun clearAll()
}
