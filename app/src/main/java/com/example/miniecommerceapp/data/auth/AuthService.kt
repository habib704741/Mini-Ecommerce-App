package com.example.miniecommerceapp.data.auth

import com.example.miniecommerceapp.data.model.User
import kotlinx.coroutines.delay

// Simple in-memory storage for users. Not persistent across app launches.
object AuthService {
    private val registeredUsers = mutableListOf<User>()
    private var currentUser: User? = null // To simulate a logged-in user

    fun getCurrentUser(): User? = currentUser

    suspend fun registerUser(username: String, email: String, password: String): Result<User> {
        delay(500) // Simulate network delay
        if (registeredUsers.any { it.email == email }) {
            return Result.failure(Exception("Registration failed: Email already in use."))
        }
        val newUser = User(username, email, password) // Password is plain for simplicity
        registeredUsers.add(newUser)
        return Result.success(newUser)
    }

    suspend fun loginUser(email: String, password: String): Result<User> {
        delay(500) // Simulate network delay
        val user = registeredUsers.find { it.email == email && it.passwordHash == password }
        return if (user != null) {
            currentUser = user // Simulate logging in
            Result.success(user)
        } else {
            Result.failure(Exception("Login failed: Invalid email or password."))
        }
    }

    suspend fun logoutUser() {
        delay(200) // Simulate network delay
        currentUser = null // Simulate logging out
    }
}