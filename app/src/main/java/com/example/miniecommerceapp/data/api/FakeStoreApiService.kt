package com.example.miniecommerceapp.data.api

import com.example.miniecommerceapp.data.model.Product
import retrofit2.http.GET
interface FakeStoreApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>
}