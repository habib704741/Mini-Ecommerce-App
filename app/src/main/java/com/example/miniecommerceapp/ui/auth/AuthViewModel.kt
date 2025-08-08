package com.example.miniecommerceapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniecommerceapp.data.auth.AuthService
import com.example.miniecommerceapp.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authService: AuthService = AuthService // Using the simulated service
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(authService.getCurrentUser()) // Initialize with current user from service
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        // Automatically check if a user is already logged in (e.g., from a previous session, simulated here)
        // In a real app, you'd persist login state (e.g., via Shared Preferences or DataStore)
        if (_currentUser.value != null) {
            _authState.value = AuthState.LoggedIn(_currentUser.value!!)
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authService.registerUser(username, email, password)
            result.onSuccess { user ->
                _authState.value = AuthState.LoggedIn(user)
                _currentUser.value = user
            }.onFailure { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Registration failed")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authService.loginUser(email, password)
            result.onSuccess { user ->
                _authState.value = AuthState.LoggedIn(user)
                _currentUser.value = user
            }.onFailure { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Login failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authService.logoutUser()
            _authState.value = AuthState.LoggedOut
            _currentUser.value = null
        }
    }

    fun clearAuthError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.LoggedOut // Reset error state to logged out
        }
    }

    sealed class AuthState {
        object Initial : AuthState()
        object Loading : AuthState()
        object LoggedOut : AuthState()
        data class LoggedIn(val user: User) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}