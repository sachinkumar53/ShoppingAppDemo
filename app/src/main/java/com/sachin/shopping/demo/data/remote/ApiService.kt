package com.sachin.shopping.demo.data.remote

import com.sachin.shopping.demo.data.remote.dto.Product
import com.sachin.shopping.demo.data.remote.dto.ProductListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("products")
    suspend fun getProducts(): ProductListResponse

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: Int
    ): Product
}