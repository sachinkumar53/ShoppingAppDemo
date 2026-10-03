package com.sachin.shopping.demo.data.remote

import com.sachin.shopping.demo.data.remote.dto.Product
import com.sachin.shopping.demo.data.remote.dto.ProductListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductListResponse

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: Int
    ): Product
}