package com.example.blisscakes.pages

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.blisscakes.DataClasses.Product
import com.example.blisscakes.R
import com.example.blisscakes.navigation.NavRoutes
import com.example.blisscakes.components.DashboardScaffold

@Composable
fun HomePage(navController: NavHostController) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val bestSellers = listOf(
        Product(1, "Cake Pops", 800.0, "Best-selling dreamy cake pops", R.drawable.cake_pops, "Dessert"),
        Product(2, "Cupcakes", 600.0, "Smooth strawberry goodness", R.drawable.cupcakes1, "Classic"),
        Product(3, "Bento Cake", 1300.0, "Galaxy themed bento cake", R.drawable.bento_cake2, "Mini"),
        Product(4, "Lemon Cake", 1300.0, "Classic favorite", R.drawable.lemon, "Classic")
    )

    val forYou = listOf(
        Product(5, "Birthday Cake", 500.0, "2 tier birthday cake", R.drawable.birthday_cake1, "Theme"),
        Product(6, "Bento Cake", 850.0, "Butterfly themed bento cake", R.drawable.bento_cake1, "Mini"),
        Product(7, "Batten burg Loaf", 1000.0, "A simple dessert", R.drawable.battenburg_loaf, "Dessert"),
        Product(8, "Coffee Cake", 1300.0, "Coffee delight", R.drawable.coffee, "Classic")
    )

    DashboardScaffold(navController = navController) { modifier ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = if (isLandscape) 8.dp else 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { navController.navigate(NavRoutes.Login) },
                    modifier = Modifier.size(if (isLandscape) 40.dp else 48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(if (isLandscape) 28.dp else 32.dp)
                    )
                }
            }

            BannerSection(navController, isLandscape)

            Spacer(modifier = Modifier.height(if (isLandscape) 16.dp else 24.dp))

            ProductSection("Best Seller", bestSellers, navController, isLandscape)
            Spacer(modifier = Modifier.height(if (isLandscape) 8.dp else 24.dp))
            ProductSection("For You", forYou, navController, isLandscape)
            Spacer(modifier = Modifier.height(if (isLandscape) 8.dp else 16.dp))
        }
    }
}

@Composable
fun BannerSection(navController: NavHostController, isLandscape: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isLandscape) 140.dp else 200.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = "Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(if (isLandscape) 16.dp else 24.dp)
        ) {
            Text(
                "SEARCH FOR YOUR TASTE",
                style = if (isLandscape)
                    MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                else
                    MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(if (isLandscape) 6.dp else 8.dp))

            Button(
                onClick = { navController.navigate(NavRoutes.Products) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.height(if (isLandscape) 36.dp else 40.dp)
            ) {
                Text(
                    "Shop Now",
                    style = if (isLandscape)
                        MaterialTheme.typography.labelMedium
                    else
                        MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun ProductSection(
    title: String,
    products: List<Product>,
    navController: NavHostController,
    isLandscape: Boolean
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(NavRoutes.Products) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = if (isLandscape)
                    MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                else
                    MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "View all",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(if (isLandscape) 20.dp else 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(if (isLandscape) 12.dp else 16.dp))

        if (isLandscape) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                val columns = (this.maxWidth / 180.dp).toInt().coerceAtLeast(2) // Properly scoped

                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .heightIn(min = 200.dp, max = 340.dp) // optional height cap
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    items(products) { product ->
                        ProductCard(product = product, navController = navController, isLandscape = true)
                    }
                }
            }
        }
         else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(products) { product ->
                        ProductCard(product = product, navController = navController, isLandscape = false)
                    }
                }
            }
        }
    }

@Composable
fun ProductCard(product: Product, navController: NavHostController, isLandscape: Boolean) {
    val cardWidth = if (isLandscape) 160.dp else 170.dp
    val imageHeight = if (isLandscape) 100.dp else 120.dp

    Card(
        modifier = Modifier
            .width(cardWidth)
            .clickable {
                navController.navigate(
                    "${NavRoutes.Detail}/${product.id}/${product.name}/${product.price}/${product.description}"
                )
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(imageHeight)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )

            Column(
                modifier = Modifier
                    .padding(if (isLandscape) 6.dp else 12.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = product.name,
                    style = if (isLandscape)
                        MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                    else
                        MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Rs. ${product.price}",
                    style = if (isLandscape)
                        MaterialTheme.typography.bodySmall
                    else
                        MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
