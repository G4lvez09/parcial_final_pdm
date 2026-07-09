package com.example.parcial_final_pdm.repository

import com.example.parcial_final_pdm.model.Product
import com.example.parcial_final_pdm.network.RetrofitClient

class ProductRepository {
    private val api = RetrofitClient.apiService

    suspend fun getProducts(): List<Product> {
        return api.getAllProducts()
    }

    suspend fun getProductById(id: Int): Product {
        return api.getProductById(id)
    }

    suspend fun getCategories(): List<String> {
        return api.getCategories()
    }
}
