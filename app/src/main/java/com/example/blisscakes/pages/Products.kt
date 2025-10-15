package com.example.blisscakes.pages

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.blisscakes.BlissCakesApplication
import com.example.blisscakes.R
import com.example.blisscakes.components.DashboardScaffold
import com.example.blisscakes.navigation.NavRoutes
import com.example.blisscakes.state.rememberProductsViewModel
import com.example.blisscakes.ui.theme.*
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun Products(navController: NavHostController) {
    val context = LocalContext.current
    val app = context.applicationContext as BlissCakesApplication

    // State management with ViewModel
    val viewModel = rememberProductsViewModel(context)
    val productsState by viewModel.productsState.collectAsStateWithLifecycle()
    val networkState by viewModel.networkState.collectAsStateWithLifecycle()
    val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()

    // Sensor data
    val sensorData by app.sensorManager.sensorData.collectAsStateWithLifecycle()
    val batteryLevel by app.batteryMonitor.batteryLevel.collectAsStateWithLifecycle()

    val config = LocalConfiguration.current
    val isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isTablet = config.screenWidthDp > 600
    val isDarkTheme = isSystemInDarkTheme()

    // Show sensor drawer
    var showSensorDrawer by remember { mutableStateOf(false) }

    DashboardScaffold(navController = navController) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                if (isDarkTheme) Color(0xFFB0B0B0) else LightPink,
                                if (isDarkTheme) Color(0xFF121212) else MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .padding(
                        top = innerPadding.calculateTopPadding() + 16.dp,
                        bottom = innerPadding.calculateBottomPadding() + 16.dp
                    )
            ) {
                // Header with profile and sensor icons
                HeaderBar(
                    navController = navController,
                    isLandscape = isLandscape,
                    networkState = networkState,
                    batteryLevel = batteryLevel,
                    onSensorClick = { showSensorDrawer = true }
                )

                // Search bar with state preserved on rotation
                SearchBar(
                    query = productsState.searchQuery,
                    onQueryChange = { viewModel.updateSearchQuery(it) }
                )

                HeaderSection(isLandscape, isDarkTheme)

                // Filter section
                FilterSection(
                    selectedFilters = productsState.selectedFilters,
                    onFilterChange = { filter -> viewModel.toggleFilter(filter) },
                    isLandscape = isLandscape
                )

                // Loading or Error state
                when {
                    productsState.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    productsState.error != null -> {
                        ErrorMessage(
                            message = productsState.error!!,
                            onRetry = { viewModel.loadProducts() }
                        )
                    }
                    else -> {
                        // Product grid
                        ProductGrid(
                            products = filteredProducts,
                            isTablet = isTablet,
                            isLandscape = isLandscape
                        ) { product ->
                            navController.navigate(
                                "detail/${product.id}/${product.name}/${product.price}/${product.description}"
                            )
                        }
                    }
                }
            }

            // Sensor drawer
            if (showSensorDrawer) {
                SensorDrawer(
                    sensorData = sensorData,
                    batteryLevel = batteryLevel,
                    onDismiss = { showSensorDrawer = false }
                )
            }
        }
    }
}

@Composable
private fun HeaderBar(
    navController: NavHostController,
    isLandscape: Boolean,
    networkState: com.example.blisscakes.state.NetworkState,
    batteryLevel: Int,
    onSensorClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Network status and battery
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Network indicator
            Icon(
                imageVector = if (networkState.isConnected) Icons.Default.Wifi else Icons.Default.WifiOff,
                contentDescription = "Network Status",
                tint = if (networkState.isConnected) Color.Green else Color.Red,
                modifier = Modifier.size(20.dp)
            )

            // Battery indicator
            IconButton(onClick = onSensorClick) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.BatteryFull,
                        contentDescription = "Battery",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "$batteryLevel%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // Profile icon
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search products...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun HeaderSection(isLandscape: Boolean, isDarkTheme: Boolean) {
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
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Baked with love, just like home. Our cakes bring the warmth of homemade goodness.",
            textAlign = TextAlign.Center,
            fontSize = if (isLandscape) 18.sp else 17.sp,
            color = MaterialTheme.colorScheme.onBackground,
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
                        labelColor = MaterialTheme.colorScheme.onSurface,
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Composable
private fun ProductGrid(
    products: List<com.example.blisscakes.data.model.Product>,
    isTablet: Boolean,
    isLandscape: Boolean,
    onProductClick: (com.example.blisscakes.data.model.Product) -> Unit
) {
    val columns = when {
        isTablet && isLandscape -> 4
        isTablet || isLandscape -> 3
        else -> 2
    }

    if (products.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No products found",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    } else {
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
                        .widthIn(
                            min = 0.dp,
                            max = (LocalConfiguration.current.screenWidthDp.dp / columns) - 24.dp
                        )
                ) {
                    ProductCard(product, onClick = { onProductClick(product) })
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: com.example.blisscakes.data.model.Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Use AsyncImage for network images or painterResource for local
            if (product.imageUrl.startsWith("http")) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.carrot) // Fallback
                )
            } else {
                // Use local drawable resource
                val resourceId = when (product.id) {
                    1 -> R.drawable.carrot
                    2 -> R.drawable.chocolate
                    3 -> R.drawable.strawberry
                    4 -> R.drawable.butter_cake
                    5 -> R.drawable.lemon
                    6 -> R.drawable.birthday_cake1
                    7 -> R.drawable.nutella_brownies
                    8 -> R.drawable.eclairs
                    9 -> R.drawable.bento_cake3
                    10 -> R.drawable.bento_cake2
                    else -> R.drawable.carrot
                }

                Image(
                    painter = painterResource(id = resourceId),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                product.name,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2
            )

            Text(
                "Rs. %.2f".format(product.price),
                color = MaterialTheme.colorScheme.primary
            )

            if (product.stock > 0) {
                Text(
                    "In Stock: ${product.stock}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Green
                )
            } else {
                Text(
                    "Out of Stock",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
private fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun SensorDrawer(
    sensorData: com.example.blisscakes.data.model.SensorData,
    batteryLevel: Int,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clickable(enabled = false) { },
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Device Sensors",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                SensorItem("Accelerometer X", String.format("%.2f", sensorData.accelerometerX))
                SensorItem("Accelerometer Y", String.format("%.2f", sensorData.accelerometerY))
                SensorItem("Accelerometer Z", String.format("%.2f", sensorData.accelerometerZ))

                Spacer(modifier = Modifier.height(8.dp))

                SensorItem("Gyroscope X", String.format("%.2f", sensorData.gyroscopeX))
                SensorItem("Gyroscope Y", String.format("%.2f", sensorData.gyroscopeY))
                SensorItem("Gyroscope Z", String.format("%.2f", sensorData.gyroscopeZ))

                Spacer(modifier = Modifier.height(8.dp))

                SensorItem("Light Level", String.format("%.2f lux", sensorData.lightLevel))
                SensorItem("Battery Level", "$batteryLevel%")
            }
        }
    }
}

@Composable
private fun SensorItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}