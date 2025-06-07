package com.example.blisscakes.DataClasses

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Product(val id: Int,
                   val name: String,
                   val price: Double,
                   val description: String,
                   val image: Int
)