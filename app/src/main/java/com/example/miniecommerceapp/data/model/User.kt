package com.example.miniecommerceapp.data.model


data class User(
    val username: String,
    val email: String,
    // Note: In a real app, passwords should never be stored in plain text and should be securely hashed.
    // For this in-memory example, we'll keep it simple.
    val passwordHash: String
)