package com.sachin.shopping.demo.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sachin.shopping.demo.data.mapper.toModel
import com.sachin.shopping.demo.data.model.Product
import com.sachin.shopping.demo.data.model.ProductListing
import com.sachin.shopping.demo.data.paging.ProductsPagingSource
import com.sachin.shopping.demo.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val apiService: ApiService
) {

    val pager = Pager(
        config = PagingConfig(30),
        initialKey = 0,
        pagingSourceFactory = { ProductsPagingSource(apiService) }
    )

    fun getAllProducts(): Flow<PagingData<ProductListing>> {
        return pager.flow
    }

    suspend fun getProductDetail(productId: Int): Product {
        return apiService.getProduct(productId).toModel()
    }
}