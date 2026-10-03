package com.sachin.shopping.demo.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sachin.shopping.demo.data.mapper.toProductListing
import com.sachin.shopping.demo.data.model.ProductListing
import com.sachin.shopping.demo.data.remote.ApiService

class ProductsPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, ProductListing>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, ProductListing> {
        return try {
            val page = params.key ?: 0
            val response = apiService.getProducts(
                limit = params.loadSize,
                skip = page * params.loadSize
            )
            val products = response.products.map { it.toProductListing() }
            LoadResult.Page(
                data = products,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (products.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductListing>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}