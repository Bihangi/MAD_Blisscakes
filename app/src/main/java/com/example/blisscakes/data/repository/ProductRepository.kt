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

class ProductRepository(private val context: Context) {

    private val database = BlissCakesDatabase.getDatabase(context)
    private val productDao = database.productDao()
    private val apiService = RetrofitClient.apiService
    private val jsonReader = JsonFileReader(context)

    suspend fun getProducts(isOnline: Boolean): Result<List<Product>> = withContext(Dispatchers.IO) {
        return@withContext try {
            if (isOnline) {
                // Try to fetch from API
                val response = safeApiCall { apiService.getProducts() }
                when (response) {
                    is ApiResponse.Success -> {
                        if (response.data.success) {
                            // Cache products to local database
                            val products = response.data.data.map { it.copy(isCached = true) }
                            productDao.insertProducts(products)
                            Result.success(products)
                        } else {
                            // Fallback to local data
                            getLocalProducts()
                        }
                    }
                    else -> {
                        // Fallback to local data
                        getLocalProducts()
                    }
                }
            } else {
                // Offline mode - use local data
                getLocalProducts()
            }
        } catch (e: Exception) {
            getLocalProducts()
        }
    }

    private suspend fun getLocalProducts(): Result<List<Product>> {
        return try {
            // First try Room database
            val cachedProducts = productDao.getAllProducts().first()
            if (cachedProducts.isNotEmpty()) {
                Result.success(cachedProducts)
            } else {
                // Fallback to local JSON file
                val jsonProducts = jsonReader.readProductsFromJson("products.json")
                productDao.insertProducts(jsonProducts)
                Result.success(jsonProducts)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to load products: ${e.message}"))
        }
    }

    suspend fun getProductById(productId: Int): Result<Product> = withContext(Dispatchers.IO) {
        return@withContext try {
            val product = productDao.getProductById(productId)
            if (product != null) {
                Result.success(product)
            } else {
                Result.failure(Exception("Product not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchProducts(query: String): Result<List<Product>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val products = productDao.searchProducts(query).first()
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}