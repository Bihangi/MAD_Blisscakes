package com.example.blisscakes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.blisscakes.pages.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = NavRoutes.Splash) {
        composable(NavRoutes.Splash){ SplashScreen(navController) }
        composable(NavRoutes.Home) { HomePage(navController) }
        composable(NavRoutes.Cart) { CartPage(navController) }
        composable(NavRoutes.Login) { LoginPage(navController) }
        composable(NavRoutes.Signup) { SignupPage(navController) }
        composable(NavRoutes.Detail) { DetailScreen(navController) }
        composable(NavRoutes.Dashboard) { UserDashboard(navController) }
        composable(NavRoutes.Summary) { ProductSummaryScreen(navController)  }
    }
}


