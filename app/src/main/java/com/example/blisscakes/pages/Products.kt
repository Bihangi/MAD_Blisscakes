package com.example.blisscakes.pages

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.example.blisscakes.DataClasses.Product
import com.example.blisscakes.R
import com.example.blisscakes.navigation.NavRoutes
import com.example.blisscakes.ui.theme.*
import com.example.blisscakes.components.DashboardScaffold
import com.google.accompanist.flowlayout.FlowRow
import androidx.compose.ui.graphics.Color

@Composable
fun Products(navController: NavHostController) {
    val config = LocalConfiguration.current
    val isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isTablet = config.screenWidthDp > 600

    // Static list of products
    val cakeProducts = remember {
        listOf(
            Product(1, "Carrot Cake", 1600.0, "Moist carrot cake with cream cheese frosting", R.drawable.carrot, "Classic"),
            Product(2, "Chocolate Cake", 1300.0, "Rich chocolate cake with dark chocolate ganache", R.drawable.chocolate, "Classic"),
            Product(3, "Strawberry Cake", 1300.0, "Fresh strawberry cake with whipped cream", R.drawable.strawberry, "Classic"),
            Product(4, "Butter Cake", 1050.0, "Classic butter cake with vanilla frosting", R.drawable.butter_cake, "Classic"),
            Product(5, "Lemon Cake", 1200.0, "Zesty lemon cake with lemon glaze", R.drawable.lemon, "Classic"),
            Product(6, "Birthday Cake", 5450.0, "2 tier cake with vanilla frosting", R.drawable.birthday_cake1, "Theme"),
            Product(7, "Nutella Brownies", 850.0, "Sweet chocolate brownies with Nutella", R.drawable.nutella_brownies, "Desserts"),
            Product(8, "Eclairs", 1250.0, "Freshly baked Eclairs", R.drawable.eclairs, "Desserts"),
            Product(9, "Make a Wish", 2850.0, "A bento cake covered in buttercream frosting", R.drawable.bento_cake3, "Mini"),
            Product(10, "Ocean Whispers", 2850.0, "A bento cake with a colorful ocean theme", R.drawable.bento_cake2, "Mini")
        )
    }

    var selectedFilters by remember { mutableStateOf(setOf<String>()) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = remember(selectedFilters, searchQuery, cakeProducts) {
        cakeProducts.filter {
            (selectedFilters.isEmpty() || selectedFilters.contains(it.category)) &&
                    (searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true))
        }
    }

    // Bottom nav and content padding
    DashboardScaffold(navController = navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                    Brush.verticalGradient(
                        listOf(
                            LightPink,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .padding(
                    top = innerPadding.calculateTopPadding() + 16.dp,
                    bottom = innerPadding.calculateBottomPadding() + 16.dp
                )
        ) {
            HeaderBar(navController, isLandscape)
            SearchBar(searchQuery) { searchQuery = it }
            HeaderSection(isLandscape)
            FilterSection(selectedFilters, { filter ->
                selectedFilters = if (selectedFilters.contains(filter))
                    selectedFilters - filter else selectedFilters + filter
            }, isLandscape)
            ProductGrid(
                products = filteredProducts,
                isTablet = isTablet,
                isLandscape = isLandscape
            ) { product ->
                navController.navigate("detail/${product.id}/${product.name}/${product.price}/${product.description}")
            }
        }
    }
}

@Composable
private fun HeaderBar(navController: NavHostController, isLandscape: Boolean) {
    // Profile icon
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(
            onClick = { navController.navigate(NavRoutes.Login) },
            modifier = Modifier.size(if (isLandscape) 40.dp else 48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(if (isLandscape) 28.dp else 32.dp)
            )
        }
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    // Search bar
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun HeaderSection(isLandscape: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (isLandscape) 12.dp else 16.dp)
    ) {
        Text(
            "Cakes",
            fontSize = if (isLandscape) 34.sp else 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Baked with love, just like home. Our cakes bring the warmth of homemade goodness.",
            textAlign = TextAlign.Center,
            fontSize = if (isLandscape) 18.sp else 16.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

@Composable
private fun FilterSection(
    selectedFilters: Set<String>,
    onFilterChange: (String) -> Unit,
    isLandscape: Boolean
) {
    val filters = listOf("Theme", "Classic", "Mini", "Desserts")
    val isDarkTheme = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "FILTER BY",
            fontSize = if (isLandscape) 16.sp else 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp)
        ) {
            filters.forEach { filter ->
                FilterChip(
                    onClick = { onFilterChange(filter) },
                    label = {
                        Text(
                            filter,
                            fontSize = if (isLandscape) 14.sp else 12.sp
                        )
                    },
                    selected = selectedFilters.contains(filter),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        labelColor = if (isDarkTheme) Color.White else MaterialTheme.colorScheme.onSurface,
                        selectedLabelColor = if (isDarkTheme) Color.White else MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Composable
private fun ProductGrid(
    products: List<Product>,
    isTablet: Boolean,
    isLandscape: Boolean,
    onProductClick: (Product) -> Unit
) {
    val columns = when {
        isTablet && isLandscape -> 4
        isTablet || isLandscape -> 3
        else -> 2
    }

    FlowRow(
        mainAxisSpacing = 16.dp,
        crossAxisSpacing = 16.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        products.forEach { product ->
            Box(
                modifier = Modifier
                    .widthIn(min = 0.dp, max = (LocalConfiguration.current.screenWidthDp.dp / columns) - 24.dp)
            ) {
                ProductCard(product, onClick = { onProductClick(product) })
            }
        }
    }
}

@Composable
internal fun ProductCard(product: Product, onClick: () -> Unit) {
    // Card representing a single product
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(product.name, fontWeight = FontWeight.Bold)
            Text("Rs. %.2f".format(product.price), color = MaterialTheme.colorScheme.primary)
        }
    }
}
