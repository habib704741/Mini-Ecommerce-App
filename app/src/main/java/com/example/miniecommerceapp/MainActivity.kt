package com.example.miniecommerceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.miniecommerceapp.navigation.NavRoutes
import com.example.miniecommerceapp.ui.auth.AuthViewModel
import com.example.miniecommerceapp.ui.auth.LoginScreen
import com.example.miniecommerceapp.ui.auth.SignupScreen
import com.example.miniecommerceapp.ui.cart.CartScreen
import com.example.miniecommerceapp.ui.checkout.CheckoutScreen
import com.example.miniecommerceapp.ui.orderhistory.OrderHistoryScreen
import com.example.miniecommerceapp.ui.productdetail.ProductDetailScreen
import com.example.miniecommerceapp.ui.productlist.ProductListScreen
import com.example.miniecommerceapp.ui.productlist.ProductViewModel
import com.example.miniecommerceapp.ui.theme.MiniEcommerceAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiniEcommerceAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authViewModel: AuthViewModel = viewModel()
                    val authState by authViewModel.authState.collectAsState()

                    when (authState) {
                        is AuthViewModel.AuthState.LoggedIn -> {
                            val mainNavController = rememberNavController()
                            val productViewModel: ProductViewModel = viewModel()
                            EcommerceNavHost(
                                navController = mainNavController,
                                productViewModel = productViewModel,
                                authViewModel = authViewModel
                            )
                        }
                        else -> {
                            val authNavController = rememberNavController()
                            AuthNavHost(
                                navController = authNavController,
                                authViewModel = authViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EcommerceNavHost(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    authViewModel: AuthViewModel
) {
    NavHost(navController = navController, startDestination = NavRoutes.PRODUCT_LIST) {
        composable(NavRoutes.PRODUCT_LIST) {
            ProductListScreen(
                viewModel = productViewModel,
                authViewModel = authViewModel,
                onNavigateToCart = { navController.navigate(NavRoutes.CART) },
                onProductClick = { productId ->
                    navController.navigate("product_detail/$productId")
                },
                onNavigateToOrderHistory = { navController.navigate(NavRoutes.ORDER_HISTORY) },
                onLogout = { authViewModel.logout() }
            )
        }
        composable(NavRoutes.CART) {
            CartScreen(
                viewModel = productViewModel,
                onProceedToCheckout = { navController.navigate(NavRoutes.CHECKOUT) }
            )
        }
        composable(
            route = NavRoutes.PRODUCT_DETAIL,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            if (productId != null) {
                ProductDetailScreen(
                    productId = productId,
                    viewModel = productViewModel,
                    onAddToCart = { product ->
                        productViewModel.addToCart(product)
                    }
                )
            } else {
                Text("Error: Product ID missing for details.", color = MaterialTheme.colorScheme.error)
            }
        }
        composable(NavRoutes.CHECKOUT) {
            CheckoutScreen(
                onOrderConfirmed = {
                    productViewModel.placeOrder()
                    navController.popBackStack(NavRoutes.PRODUCT_LIST, inclusive = false)
                }
            )
        }
        composable(NavRoutes.ORDER_HISTORY) {
            OrderHistoryScreen(viewModel = productViewModel)
        }
    }
}


@Composable
fun AuthNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(navController = navController, startDestination = NavRoutes.LOGIN) {
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignup = { navController.navigate(NavRoutes.SIGNUP) },
                onLoginSuccess = {
                }
            )
        }
        composable(NavRoutes.SIGNUP) {
            SignupScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(NavRoutes.LOGIN) },
                onSignupSuccess = {
                }
            )
        }
    }
}