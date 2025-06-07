package com.example.blisscakes

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AuthenticationViewModel : ViewModel() {
    val isAuthenticated = mutableStateOf(false)

    fun login(email: String, password: String) {
        // For now, we just toggle authentication
        isAuthenticated.value = true
    }
}
