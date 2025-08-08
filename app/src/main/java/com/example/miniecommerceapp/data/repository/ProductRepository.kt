package com.example.miniecommerceapp.data.repository

import com.example.miniecommerceapp.data.api.ApiClient
import com.example.miniecommerceapp.data.model.Product

class ProductRepository(private val apiService: ApiClient) {
    suspend fun getProducts(): List<Product> {
        return apiService.fakeStoreService.getProducts()
    }
}

