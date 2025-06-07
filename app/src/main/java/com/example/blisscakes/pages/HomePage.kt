
// HomePage.kt
package com.example.blisscakes.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.blisscakes.DataClasses.Product
import com.example.blisscakes.R
import com.example.blisscakes.navigation.NavRoutes

@Composable
fun HomePage(navController: NavHostController) {
    val bestSellers = listOf(
        Product(1, "Chocolate Cheese Cake", 800.0, "Best-selling creamy cake", R.drawable.cart1),
        Product(2, "Chocolate Fudge", 600.0, "Rich chocolate goodness", R.drawable.cart2),
        Product(3, "Coconut Cake", 1300.0, "Tropical delight", R.drawable.cart3),
        Product(4, "Carrot Cake", 1300.0, "Classic favorite", R.drawable.cart3)
    )

    val forYou = listOf(
        Product(5, "Russian Honey Cake", 500.0, "Layered honey flavor", R.drawable.cart1),
        Product(6, "Strawberry Mousse Cake", 850.0, "Fruity and fresh", R.drawable.cart2),
        Product(7, "Chocolate Cupcake 6pcs", 1000.0, "Pack of cupcakes", R.drawable.cart3),
        Product(8, "Coconut Cake", 1300.0, "Tropical delight", R.drawable.cart3)
    )

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Header with profile icon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { navController.navigate(NavRoutes.Login) },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Banner Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isLandscape) 250.dp else 200.dp)
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
                    .padding(24.dp)
            ) {
                Text(
                    "SEARCH FOR YOUR TASTE",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { /* Handle shop now */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Shop Now")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Best Sellers Section
        ProductSection(title = "Best Seller", products = bestSellers, navController)

        Spacer(modifier = Modifier.height(24.dp))

        // For You Section
        ProductSection(title = "For You", products = forYou, navController)

        Spacer(modifier = Modifier.height(24.dp))

        // Footer
        FooterSection()
    }
}

@Composable
fun ProductSection(title: String, products: List<Product>, navController: NavHostController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "View all",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products.size) { index ->
                ProductCard(product = products[index], navController)
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, navController: NavHostController) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { navController.navigate("${NavRoutes.Detail}/${product.id}") },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = product.image),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Rs. ${product.price}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun FooterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Quick Links
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Home", "About", "Cakes", "Checkout", "Contact").forEach { item ->
                TextButton(onClick = { /* Handle navigation */ }) {
                    Text(
                        text = item,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contact Info
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Email: blisscakes@gmail.com",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Mobile: +94 72 123 1234",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Address: BlissCakes, KCC, Kandy",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Copyright
        Text(
            text = "Copyright @2025. Designed by BlissCakes",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}