package com.example.blisscakes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// User Models
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val token: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class AuthResponse(
    val success: Boolean,
    val message: String?,
    val data: UserData?
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String,
    val token: String
)

// Product Models
@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val imageUrl: String,
    val category: String,
    val stock: Int = 0,
    val isCached: Boolean = false
)

data class ProductResponse(
    val success: Boolean,
    val data: List<Product>
)

// Sensor Data Models
data class SensorData(
    val accelerometerX: Float = 0f,
    val accelerometerY: Float = 0f,
    val accelerometerZ: Float = 0f,
    val gyroscopeX: Float = 0f,
    val gyroscopeY: Float = 0f,
    val gyroscopeZ: Float = 0f,
    val lightLevel: Float = 0f,
    val batteryLevel: Int = 0,
    val isCharging: Boolean = false
)

data class LocationData(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val accuracy: Float = 0f,
    val address: String = ""
)

// Network Models
data class ApiError(
    val code: Int,
    val message: String
)

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val code: Int? = null) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}