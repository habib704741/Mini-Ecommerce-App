package com.example.miniecommerceapp.ui.productdetail


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

// **CRUCIAL IMPORTS:**
import com.example.miniecommerceapp.data.model.Product // Ensure Product data class is imported
import com.example.miniecommerceapp.ui.productlist.ProductViewModel // Ensure ProductViewModel is imported
import com.example.miniecommerceapp.ui.productlist.ProductUiState // Ensure nested ProductUiState is imported


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    viewModel: ProductViewModel = viewModel(), // We use the same ViewModel to access product data
    onAddToCart: (Product) -> Unit // Lambda to add to cart (takes Product object)
) {
    val uiState by viewModel.uiState.collectAsState()
    val product: Product? = (uiState as? ProductUiState.Success)
        ?.products?.find { it.id == productId }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(product?.title ?: "Product Details") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Make screen scrollable for long descriptions
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (product == null) {
                // Handle case where product is not found or still loading
                CircularProgressIndicator() // Or a "Product not found" message
                Spacer(modifier = Modifier.height(16.dp))
                Text("Loading product or product not found...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                // Product Image
                AsyncImage(
                    model = product.image,
                    contentDescription = product.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp) // Adjust height as needed
                        .padding(bottom = 16.dp)
                )

                // Product Title
                Text(
                    text = product.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Price
                Text(
                    text = "$%.2f".format(product.price),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Category
                Text(
                    text = product.category.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }, // Capitalize first letter
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Rating: ${"%.1f".format(product.rating.rate)}", // Format to one decimal
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${product.rating.count} reviews)",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = product.description,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start, // Align description to start
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Add to Cart Button
                Button(
                    onClick = { onAddToCart(product) }, // Pass the whole product object
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Add to Cart", fontSize = 18.sp)
                }
            }
        }
    }
}