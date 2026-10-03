package com.sachin.shopping.demo.data.repository

import com.sachin.shopping.demo.data.mapper.toProductListing
import com.sachin.shopping.demo.data.model.ProductListing
import com.sachin.shopping.demo.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getAllProducts(): List<ProductListing> {
        return apiService.getProducts().products.map { it.toProductListing() }
    }
}