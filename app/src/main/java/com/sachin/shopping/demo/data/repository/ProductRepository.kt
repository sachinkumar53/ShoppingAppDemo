package com.sachin.shopping.demo.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.sachin.shopping.demo.data.local.AppDatabase
import com.sachin.shopping.demo.data.mapper.toEntity
import com.sachin.shopping.demo.data.mapper.toModel
import com.sachin.shopping.demo.data.model.Product
import com.sachin.shopping.demo.data.paging.ProductRemoteMediator
import com.sachin.shopping.demo.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val apiService: ApiService,
    private val db: AppDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getAllProducts() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        remoteMediator = ProductRemoteMediator(apiService, db),
        pagingSourceFactory = { db.productDao().pagingSource() }
    ).flow

    suspend fun getProductDetail(productId: Int): Product {
        return try {
            val remote = apiService.getProduct(productId).toModel()
            db.productDao().upsert(remote.toEntity())
            remote
        } catch (e: Exception) {
            val cached = db.productDao().getProduct(productId)
            cached?.toModel() ?: throw e
        }
    }
}
