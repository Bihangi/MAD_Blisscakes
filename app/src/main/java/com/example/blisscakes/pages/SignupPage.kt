package com.example.blisscakes.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.blisscakes.R
import com.example.blisscakes.navigation.NavRoutes

@Composable
fun SignupPage(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "SIGN UP",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(bottom = 32.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Full Name
                    Text("Full Name", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start), color = MaterialTheme.colorScheme.onSurface)
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Enter your full name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    // Email
                    Text("Email", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start), color = MaterialTheme.colorScheme.onSurface)
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = !it.contains("@")
                        },
                        isError = emailError,
                        placeholder = { Text("Enter your email") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = if (emailError) 4.dp else 16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    if (emailError) {
                        Text(
                            text = "Please enter a valid email address.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(bottom = 12.dp)
                        )
                    }

                    // Password
                    Text("Password", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start), color = MaterialTheme.colorScheme.onSurface)
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = it.length < 6
                        },
                        isError = passwordError,
                        placeholder = { Text("Enter your password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = if (passwordError) 4.dp else 0.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    if (passwordError) {
                        Text(
                            text = "Password must be at least 6 characters.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(bottom = 12.dp)
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 32.dp).fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )

                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            if (!emailError && !passwordError && name.isNotBlank()) {
                                navController.navigate(NavRoutes.Home)
                            }
                        },
                        enabled = name.isNotBlank() && email.isNotBlank() && password.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("SIGN UP")
                    }

                    TextButton(
                        onClick = { navController.navigate(NavRoutes.Login) },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Already have an account? Login here", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
