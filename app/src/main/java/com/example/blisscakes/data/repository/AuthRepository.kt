package com.example.blisscakes.data.repository

import android.content.Context
import com.example.blisscakes.data.local.BlissCakesDatabase
import com.example.blisscakes.data.local.UserPreferencesManager
import com.example.blisscakes.data.model.*
import com.example.blisscakes.data.remote.ApiResponse
import com.example.blisscakes.data.remote.RetrofitClient
import com.example.blisscakes.data.remote.safeApiCall
import com.example.blisscakes.utils.JsonFileReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AuthRepository(private val context: Context) {

    private val preferencesManager = UserPreferencesManager(context)
    private val apiService = RetrofitClient.apiService

    suspend fun login(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        return@withContext try {
            val request = LoginRequest(email, password)
            val response = safeApiCall { apiService.login(request) }

            when (response) {
                is ApiResponse.Success -> {
                    if (response.data.success && response.data.data != null) {
                        val userData = response.data.data
                        preferencesManager.saveUserData(
                            userId = userData.id,
                            name = userData.name,
                            email = userData.email,
                            token = userData.token
                        )
                        Result.success(
                            User(
                                id = userData.id,
                                name = userData.name,
                                email = userData.email,
                                token = userData.token
                            )
                        )
                    } else {
                        Result.failure(Exception(response.data.message ?: "Login failed"))
                    }
                }
                is ApiResponse.Error -> {
                    Result.failure(Exception(response.message))
                }
                is ApiResponse.NetworkError -> {
                    Result.failure(Exception("Network error. Please check your connection."))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val request = RegisterRequest(name, email, password)
                val response = safeApiCall { apiService.register(request) }

                when (response) {
                    is ApiResponse.Success -> {
                        if (response.data.success && response.data.data != null) {
                            val userData = response.data.data
                            preferencesManager.saveUserData(
                                userId = userData.id,
                                name = userData.name,
                                email = userData.email,
                                token = userData.token
                            )
                            Result.success(
                                User(
                                    id = userData.id,
                                    name = userData.name,
                                    email = userData.email,
                                    token = userData.token
                                )
                            )
                        } else {
                            Result.failure(Exception(response.data.message ?: "Registration failed"))
                        }
                    }
                    is ApiResponse.Error -> {
                        Result.failure(Exception(response.message))
                    }
                    is ApiResponse.NetworkError -> {
                        Result.failure(Exception("Network error. Please check your connection."))
                    }
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun logout() {
        preferencesManager.clearUserData()
    }

    suspend fun isLoggedIn(): Boolean {
        return preferencesManager.userPreferences.first().isLoggedIn
    }
}