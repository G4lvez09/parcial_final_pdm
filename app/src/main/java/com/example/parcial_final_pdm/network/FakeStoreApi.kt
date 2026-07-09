package com.example.parcial_final_pdm.network

import com.example.parcial_final_pdm.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreApi {
    @GET("products")
    suspend fun getAllProducts(): List<Product>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    @GET("products/categories")
    suspend fun getCategories(): List<String>
}