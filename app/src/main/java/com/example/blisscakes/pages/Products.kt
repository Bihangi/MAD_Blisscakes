package com.example.blisscakes.pages

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.blisscakes.DataClasses.Product
import com.example.blisscakes.R
import com.example.blisscakes.navigation.NavRoutes
import com.example.blisscakes.ui.theme.*


@Composable
fun Products(navController: NavHostController) {
    val config = LocalConfiguration.current
    val isTablet = config.screenWidthDp > 600
    val isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE

    val cakeProducts = remember {
        listOf(
            Product(1, "Carrot Cake", 1600.0, "Moist carrot cake with cream cheese frosting", R.drawable.birthday_cake1, "Classic Cakes"),
            Product(2, "Chocolate Cake", 1300.0, "Rich chocolate cake with dark chocolate ganache", R.drawable.birthday_cake1, "Classic Cakes"),
            Product(3, "Strawberry Cake", 1300.0, "Fresh strawberry cake with whipped cream", R.drawable.birthday_cake1, "Theme Cakes"),
            Product(4, "Butter Cake", 1050.0, "Classic butter cake with vanilla frosting", R.drawable.birthday_cake1, "Classic Cakes"),
            Product(5, "Lemon Cake", 1200.0, "Zesty lemon cake with lemon glaze", R.drawable.lemon, "Classic Cakes"),
            Product(6, "Orange Cake", 1450.0, "Citrus orange cake with orange buttercream", R.drawable.birthday_cake1, "Classic Cakes")
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LightPink, MaterialTheme.colorScheme.background)))
    ) {
        // Header with logo & profile
        HeaderBar(navController)

        // Search Bar
        SearchBar(searchQuery) { searchQuery = it }

        // Content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { HeaderSection() }
            item {
                FilterSection(selectedFilters) { filter ->
                    selectedFilters = if (selectedFilters.contains(filter))
                        selectedFilters - filter else selectedFilters + filter
                }
            }
            item {
                ProductGrid(
                    products = filteredProducts,
                    isTablet = isTablet,
                    isLandscape = isLandscape,
                    onProductClick = { product ->
                        navController.navigate("detail/${product.id}/${product.name}/${product.price}/${product.description}")
                    }
                )
            }
        }
    }
}

@Composable
private fun HeaderBar(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SoftPink.copy(alpha = 0.7f))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.CenterStart)) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("BLISS CAKE", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        IconButton(
            onClick = { navController.navigate(NavRoutes.Login) },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(Icons.Default.Person, contentDescription = "Profile")
        }
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
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
            colors = OutlinedTextFieldDefaults.colors()
        )
    }
}

@Composable
private fun HeaderSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 16.dp)) {
        Text("Cakes", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Baked with love, just like home. Our cakes bring the warmth of homemade goodness.",
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun FilterSection(
    selectedFilters: Set<String>,
    onFilterChange: (String) -> Unit
) {
    val filters = listOf("Theme Cakes", "Classic Cakes", "Mini Cakes", "Desserts")

    Column {
        Text("FILTER BY", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(filters) { filter ->
                FilterChip(
                    onClick = { onFilterChange(filter) },
                    label = { Text(filter, fontSize = 12.sp) },
                    selected = selectedFilters.contains(filter)
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

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.height(((products.size / columns + 1) * 280).dp)
    ) {
        items(products) { product ->
            ProductCard(product, onClick = { onProductClick(product) })
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftPink.copy(alpha = 0.3f))
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