package com.example.miniecommerceapp.ui.cart


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
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
import com.example.miniecommerceapp.data.model.CartItem
import com.example.miniecommerceapp.ui.productlist.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: ProductViewModel = viewModel(),
    onProceedToCheckout: () -> Unit // Added lambda for navigation
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val cartTotal by viewModel.cartTotal.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Cart") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (cartItems.isEmpty()) {
                Text(
                    text = "Your cart is empty!",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(cartItems) { cartItem ->
                        CartItemCard(cartItem = cartItem, viewModel = viewModel)
                    }
                }

                Divider(modifier = Modifier.fillMaxWidth().height(1.dp).padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total:", style = MaterialTheme.typography.headlineMedium)
                    Text(
                        text = "$%.2f".format(cartTotal),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onProceedToCheckout, // Use the passed lambda here
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Proceed to Checkout", fontSize = 18.sp)
                }
            }
        }
    }
}
@Composable
fun CartItemCard(cartItem: CartItem, viewModel: ProductViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            AsyncImage(
                model = cartItem.product.image,
                contentDescription = cartItem.product.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Product Title
                Text(
                    text = cartItem.product.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Product Price
                Text(
                    text = "$%.2f".format(cartItem.product.price),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            // Quantity Controls
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.decrementQuantity(cartItem.product.id) }) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease Quantity")
                }
                Text(
                    text = "${cartItem.quantity}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(30.dp),
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = { viewModel.incrementQuantity(cartItem.product.id) }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
                }
                IconButton(onClick = { viewModel.removeFromCart(cartItem.product.id) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove Item", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
