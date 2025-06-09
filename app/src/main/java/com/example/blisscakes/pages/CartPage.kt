package com.example.blisscakes.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.blisscakes.DataClasses.CartItems
import com.example.blisscakes.R
import com.example.blisscakes.navigation.NavRoutes

@Composable
fun CartPage(navController: NavHostController) {
    val cartItems = listOf(
        CartItems(1, "Chocolate Cake", 1, 800.0, R.drawable.birthday_cake1),
        CartItems(2, "Cupcake Box", 1, 1000.0, R.drawable.bento_cake1)
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Your Cart", style = MaterialTheme.typography.headlineSmall, color = Color(0xFFE91E63))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(cartItems) { item ->
                Row(Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)) {
                    Image(painter = painterResource(item.image), contentDescription = item.productName, modifier = Modifier.size(60.dp))
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(item.productName)
                        Text("Qty: ${item.quantity}")
                        Text("Price: Rs. ${item.price}")
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate(NavRoutes.Products) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Checkout", color = Color.White)
        }
    }
}
