package com.example.miniecommerceapp.ui.orderhistory


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miniecommerceapp.data.model.Order
import com.example.miniecommerceapp.ui.productlist.ProductViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    viewModel: ProductViewModel = viewModel()
) {
    val orderHistory by viewModel.orderHistory.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Order History") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (orderHistory.isEmpty()) {
                Text(
                    text = "No orders placed yet.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(orderHistory) { order ->
                        OrderItemCard(order = order)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Order ID: ${order.id.substring(0, 8)}...", // Show truncated ID
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))

            val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
            Text(
                text = "Date: ${dateFormat.format(order.orderDate)}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Display items in the order
            order.items.forEach { cartItem ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically // Added for better vertical alignment
                ) {
                    Text(
                        text = "${cartItem.quantity} x ${cartItem.product.title}",
                        fontSize = 14.sp,
                        maxLines = 1, // Restrict to one line
                        overflow = TextOverflow.Ellipsis, // Add ellipsis if text overflows
                        modifier = Modifier.weight(1f) // Let this text take available space
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Add a small space between them
                    Text(
                        text = "$%.2f".format(cartItem.product.price * cartItem.quantity),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold, // Make price a bit bolder
                        textAlign = TextAlign.End // Ensure price is right-aligned
                    )
                }
            }
            Divider(modifier = Modifier.fillMaxWidth().height(1.dp).padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$%.2f".format(order.totalAmount),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
