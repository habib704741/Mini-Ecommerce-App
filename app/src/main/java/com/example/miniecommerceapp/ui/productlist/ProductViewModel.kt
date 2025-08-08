package com.example.miniecommerceapp.ui.productlist


import com.example.miniecommerceapp.data.model.Product
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniecommerceapp.data.api.ApiClient
import com.example.miniecommerceapp.data.model.CartItem
import com.example.miniecommerceapp.data.model.Order
import com.example.miniecommerceapp.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val products: List<Product>) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
    object Initial : ProductUiState()
}

class ProductViewModel(
    // We pass ApiClient here and create repository inside for simplicity in this example
    private val repository: ProductRepository = ProductRepository(ApiClient)
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Initial)
    val uiState: StateFlow<ProductUiState> = _uiState

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Calculate total cart value
    val cartTotal: StateFlow<Double> = MutableStateFlow(0.0).apply {
        viewModelScope.launch {
            _cartItems.collect { items ->
                value = items.sumOf { it.product.price * it.quantity }
            }
        }
    }

    private val _orderHistory = MutableStateFlow<List<Order>>(emptyList())
    val orderHistory: StateFlow<List<Order>> = _orderHistory.asStateFlow()

    init {
        // Fetch products as soon as the ViewModel is created
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading
            try {
                val products = repository.getProducts()
                _uiState.value = ProductUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = ProductUiState.Error(e.message ?: "Failed to load products")
            }
        }
    }

    fun addToCart(product: Product) {
        _cartItems.value = _cartItems.value.toMutableList().apply {
            val existingItem = find { it.product.id == product.id }
            if (existingItem == null) {
                add(CartItem(product, 1))
            } else {
                existingItem.quantity++
            }
        }
    }

    fun incrementQuantity(productId: Int) {
        _cartItems.value = _cartItems.value.map { item ->
            if (item.product.id == productId && item.quantity < 99) { // Optional: Max quantity limit
                item.copy(quantity = item.quantity + 1) // Create a copy with updated quantity
            } else {
                item
            }
        }
    }

    fun decrementQuantity(productId: Int) {
        _cartItems.value = _cartItems.value.mapNotNull { item ->
            if (item.product.id == productId) {
                if (item.quantity > 1) {
                    item.copy(quantity = item.quantity - 1) // Create a copy with updated quantity
                } else {
                    null // Remove item if quantity becomes 0
                }
            } else {
                item
            }
        }
    }

    fun placeOrder() {
        val currentCartItems = _cartItems.value
        val currentCartTotal = _cartItems.value.sumOf { it.product.price * it.quantity }

        if (currentCartItems.isNotEmpty()) {
            val newOrder = Order(
                id = UUID.randomUUID().toString(), // Generate a unique ID
                items = currentCartItems.toList(), // Make a copy of cart items for the order
                totalAmount = currentCartTotal,
                orderDate = Date().time // Current timestamp
            )
            _orderHistory.value = _orderHistory.value + newOrder // Add new order to history
            clearCart() // Clear the cart after placing the order
        }
    }

    fun removeFromCart(productId: Int) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}