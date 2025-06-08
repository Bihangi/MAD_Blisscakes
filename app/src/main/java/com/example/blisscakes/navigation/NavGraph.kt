package com.example.blisscakes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.navArgument
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
        composable(NavRoutes.Products) { Products(navController)  }
        composable(
            route = "${NavRoutes.Detail}/{id}/{name}/{price}/{description}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType },
                navArgument("price") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            val name = backStackEntry.arguments?.getString("name")
            val price = backStackEntry.arguments?.getString("price")
            val description = backStackEntry.arguments?.getString("description")

            DetailScreen(navController, id, name, price, description)
        }

    }
}


