package com.sachin.shopping.demo.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sachin.shopping.demo.data.local.dao.ProductDao
import com.sachin.shopping.demo.data.mapper.toEntity
import com.sachin.shopping.demo.data.mapper.toProductListing
import com.sachin.shopping.demo.data.model.ProductListing
import com.sachin.shopping.demo.data.remote.ApiService

class ProductsPagingSource(
    private val apiService: ApiService,
    private val productDao: ProductDao
) : PagingSource<Int, ProductListing>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, ProductListing> {
        val page = params.key ?: 0
        return try {
            val response = apiService.getProducts(
                limit = params.loadSize,
                skip = page * params.loadSize
            )
            val entities = response.products.map { it.toEntity() }
            productDao.upsertAll(entities)
            val products = entities.map { it.toProductListing() }
            LoadResult.Page(
                data = products,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (products.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            // Offline fallback: load cached products from Room
            val cached = productDao.getAllEntities()
            if (cached.isNotEmpty() && page == 0) {
                LoadResult.Page(
                    data = cached.map { it.toProductListing() },
                    prevKey = null,
                    nextKey = null
                )
            } else {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductListing>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
