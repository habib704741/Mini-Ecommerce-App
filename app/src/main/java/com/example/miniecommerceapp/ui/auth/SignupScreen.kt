// app/src/main/java/com/example/miniecommerceapp/ui/auth/SignupScreen.kt (UPDATED)

package com.example.miniecommerceapp.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.* // Make sure this import is present
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miniecommerceapp.ui.auth.AuthViewModel.AuthState
import kotlinx.coroutines.launch // Make sure this import is present

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    authViewModel: AuthViewModel = viewModel(),
    onNavigateToLogin: () -> Unit,
    onSignupSuccess: () -> Unit // Lambda to navigate to main app or login
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Use rememberCoroutineScope to launch coroutines within the composable's lifecycle
    val scope = rememberCoroutineScope() // <-- ADD THIS LINE

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.LoggedIn -> {
                onSignupSuccess() // Navigates to main app upon successful signup
            }
            is AuthState.Error -> {
                snackbarHostState.showSnackbar((authState as AuthState.Error).message)
                authViewModel.clearAuthError()
            }
            else -> {}
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (password == confirmPassword) {
                        authViewModel.register(username, email, password)
                    } else {
                        // Launch a coroutine to call the suspend function showSnackbar
                        scope.launch { // <-- WRAP IN A COROUTINE SCOPE
                            snackbarHostState.showSnackbar("Passwords do not match.")
                        }
                        // It's generally better not to call authViewModel.register
                        // with empty strings for a password mismatch, as that
                        // might incorrectly trigger a backend/service error.
                        // Handle the mismatch purely on the UI layer.
                    }
                },
                enabled = authState != AuthState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Sign Up", fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? Login", fontSize = 16.sp)
            }
        }
    }
}