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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

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

    fun observeProduct(id: Int): Flow<Product?> {
        return db.productDao().observe(id).map { it?.toModel() }
    }

    suspend fun refreshProduct(id: Int): Result<Unit> = try {
        val remote = apiService.getProduct(id).toModel()
        db.productDao().upsert(remote.toEntity())
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}
