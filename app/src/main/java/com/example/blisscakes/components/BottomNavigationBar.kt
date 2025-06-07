package com.example.blisscakes.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.blisscakes.DataClasses.NavItem
import com.example.blisscakes.R
import com.example.blisscakes.navigation.NavRoutes

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavItem("Home", R.drawable.ic_home, "Home"),
        NavItem("Cart", R.drawable.ic_cart, "Cart")
    )

    NavigationBar(containerColor = Color.White) {
        val currentRoute = navController.currentDestination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(item.iconResId), contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(NavRoutes.Dashboard) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFE91E63),
                    indicatorColor = Color(0xFFFFF0F5),
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}
