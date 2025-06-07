package com.example.blisscakes.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.example.blisscakes.R
import com.example.blisscakes.navigation.NavRoutes

@Composable
fun DetailScreen(navController: NavHostController) {
    val productName = "Chocolate Cheese Cake"
    val productPrice = 800.0
    val productDescription = "A rich, creamy chocolate cheesecake perfect for celebrations. Topped with ganache & chocolate shards."
    val productImage = R.drawable.cart1

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF1F4))
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = productImage),
            contentDescription = productName,
            modifier = Modifier.fillMaxWidth().height(240.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(productName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Rs. $productPrice", fontSize = 18.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Description", style = MaterialTheme.typography.headlineSmall, color = Color(0xFFE91E63))
        Text(productDescription, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { navController.navigate(NavRoutes.Cart) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
        ) {
            Text("Add to Cart", color = Color.White)
        }
    }
}
