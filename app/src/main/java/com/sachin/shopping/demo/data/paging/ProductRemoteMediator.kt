package com.sachin.shopping.demo.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room3.withWriteTransaction
import com.sachin.shopping.demo.data.local.AppDatabase
import com.sachin.shopping.demo.data.local.entity.ProductEntity
import com.sachin.shopping.demo.data.local.entity.ProductRemoteKey
import com.sachin.shopping.demo.data.mapper.toEntity
import com.sachin.shopping.demo.data.remote.ApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator(
    private val apiService: ApiService,
    private val db: AppDatabase
) : RemoteMediator<Int, ProductEntity>() {

    private val keyDao = db.remoteKeyDao()
    private val productDao = db.productDao()

    // Skip the network refresh if the cache is fresh (offline-first startup)
    override suspend fun initialize(): InitializeAction {
        val last = keyDao.lastCreatedAt() ?: return InitializeAction.LAUNCH_INITIAL_REFRESH
        val fresh = System.currentTimeMillis() - last < CACHE_TTL_MS
        return if (fresh) InitializeAction.SKIP_INITIAL_REFRESH
        else InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        val skip = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = false)
                val key = keyDao.get(lastItem.id)
                key?.nextSkip
                    ?: return MediatorResult.Success(endOfPaginationReached = key != null)
            }
        }

        return try {
            val response = apiService.getProducts(limit = state.config.pageSize, skip = skip)
            val endReached = response.products.isEmpty() ||
                    skip + response.products.size >= response.total
            val nextSkip = if (endReached) null else skip + response.products.size

            db.withWriteTransaction {
                /*if (loadType == LoadType.REFRESH) {
                    productDao.clearAll()
                    keyDao.clear()
                }*/

                productDao.upsertAll(
                    response.products.map { it.toEntity() }
                )
                keyDao.upsertAll(
                    response.products.map { ProductRemoteKey(it.id, nextSkip) }
                )
            }
            MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private companion object {
        const val CACHE_TTL_MS = 60 * 60 * 1000L // 1 hour
    }
}