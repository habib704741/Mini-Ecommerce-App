package com.example.miniecommerceapp.data.model


data class Order(
    val id: String, // Unique ID for the order, e.g., timestamp or UUID
    val items: List<CartItem>,
    val totalAmount: Double,
    val orderDate: Long // Timestamp in milliseconds
)