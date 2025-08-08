package com.example.miniecommerceapp.data.api

import com.example.miniecommerceapp.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Log request and response bodies
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.FAKESTORE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient) // Add the OkHttpClient with logging
            .build()
    }

    val fakeStoreService: FakeStoreApiService by lazy {
        retrofit.create(FakeStoreApiService::class.java)
    }
}