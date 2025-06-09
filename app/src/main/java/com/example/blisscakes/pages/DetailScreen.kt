package com.example.blisscakes.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.blisscakes.R
import com.example.blisscakes.components.BottomNavigationBar
import com.example.blisscakes.navigation.NavRoutes
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    navController: NavHostController,
    productId: Int?,
    productName: String?,
    productPrice: String?,
    productDescription: String?
) {
    val imageRes = R.drawable.carrot
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    var quantity by remember { mutableStateOf(1) }
    var message by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.background),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .clickable { navController.popBackStack() }
                            .size(32.dp)
                    )

                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = productName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp, max = 300.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                ) {
                    DetailContent(
                        name = productName ?: "Cake",
                        price = productPrice ?: "0.00",
                        description = productDescription ?: "No description available",
                        quantity = quantity,
                        onQuantityChange = { quantity = it },
                        message = message,
                        onMessageChange = { message = it },
                        onAddToCart = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Item added to cart")
                            }
                        },
                        onCheckout = { navController.navigate(NavRoutes.Cart) }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back",
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { navController.popBackStack() }
                        .size(32.dp)
                )

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = productName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )

                DetailContent(
                    name = productName ?: "Cake",
                    price = productPrice ?: "0.00",
                    description = productDescription ?: "No description available",
                    quantity = quantity,
                    onQuantityChange = { quantity = it },
                    message = message,
                    onMessageChange = { message = it },
                    onAddToCart = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Item added to cart")
                        }
                    },
                    onCheckout = { navController.navigate(NavRoutes.Cart) }
                )
            }
        }
    }
}

@Composable
fun DetailContent(
    name: String,
    price: String,
    description: String,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    message: String,
    onMessageChange: (String) -> Unit,
    onAddToCart: () -> Unit,
    onCheckout: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Rs. $price", fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
        }

        Text(description, fontSize = 14.sp, lineHeight = 20.sp)

        Text("Quantity", fontWeight = FontWeight.SemiBold)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("-", fontSize = 20.sp)
            }
            Text(quantity.toString(), fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Button(
                onClick = { onQuantityChange(quantity + 1) },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("+", fontSize = 20.sp)
            }
        }

        Text("Message on cake", fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Message On Cake") },
            shape = RoundedCornerShape(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = onAddToCart,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("Add to Cart")
            }

            Button(
                onClick = onCheckout,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Checkout", color = Color.White)
            }
        }
    }
}
