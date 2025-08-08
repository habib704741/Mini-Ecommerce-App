package com.example.miniecommerceapp.ui.productlist

import androidx.compose.foundation.clickable // Ensure this import is present
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.miniecommerceapp.data.model.Product
import com.example.miniecommerceapp.ui.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: ProductViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(), // Pass AuthViewModel
    onNavigateToCart: () -> Unit,
    onProductClick: (Int) -> Unit,
    onNavigateToOrderHistory: () -> Unit,
    onLogout: () -> Unit // New parameter for logout action
) {
    val uiState by viewModel.uiState.collectAsState()
    val cartItemsCount by viewModel.cartItems.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Our Products") },
                actions = { // Add actions for top app bar
                    IconButton(onClick = onNavigateToOrderHistory) {
                        Icon(Icons.Default.History, contentDescription = "Order History")
                    }
                    IconButton(onClick = onLogout) { // Logout Button
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToCart,
                icon = { Icon(Icons.Filled.ShoppingCart, "Shopping cart") },
                text = { Text("Cart (${cartItemsCount.sumOf { it.quantity }})") }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (val state = uiState) {
                is ProductUiState.Initial -> {
                    Text(
                        text = "Fetching products...",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                is ProductUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is ProductUiState.Success -> {
                    if (state.products.isEmpty()) {
                        Text(
                            text = "No products found.",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.products) { product ->
                                ProductItem(
                                    product = product,
                                    onAddToCart = { viewModel.addToCart(it) },
                                    onProductClick = onProductClick
                                )
                            }
                        }
                    }
                }
                is ProductUiState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = { viewModel.fetchProducts() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onAddToCart: (Product) -> Unit, // Takes Product object
    onProductClick: (Int) -> Unit // Takes productId
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick(product.id) } // Make the card clickable
            .height(260.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$%.2f".format(product.price),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onAddToCart(product) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to Cart")
            }
        }
    }
}