package com.example.blisscakes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.blisscakes.DataClasses.NavItem
import com.example.blisscakes.R
import com.example.blisscakes.navigation.NavRoutes

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavItem(NavRoutes.Home, R.drawable.ic_home, "Home"),
        NavItem(NavRoutes.Products, R.drawable.ic_cake, "Cakes"),
        NavItem(NavRoutes.Cart, R.drawable.ic_cart, "Cart"),
        NavItem(NavRoutes.Login, R.drawable.ic_user, "Logout")
    )

    val config = LocalConfiguration.current
    val isLandscape = config.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(bottom = if (isLandscape) 2.dp else 4.dp)
    ) {
        NavigationBar(
            containerColor = Color.White,
            modifier = Modifier.height(if (isLandscape) 70.dp else 80.dp)
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(item.iconResId),
                            contentDescription = item.label,
                            modifier = Modifier.size(if (isLandscape) 20.dp else 24.dp)
                        )
                    },
                    label = {
                        Text(
                            item.label,
                            style = if (isLandscape)
                                MaterialTheme.typography.labelSmall
                            else
                                MaterialTheme.typography.labelMedium
                        )
                    },
                    selected = navController.currentDestination?.route == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(0) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFE91E63),
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color(0xFFFFF0F5),
                        selectedTextColor = Color(0xFFE91E63),
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }

        // Copyright Footer
        if (!isLandscape) {
            Text(
                text = "© 2025 BlissCakes",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 2.dp, bottom = 2.dp)
            )
        }
    }
}