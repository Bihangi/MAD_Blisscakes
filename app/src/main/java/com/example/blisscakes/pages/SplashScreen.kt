package com.example.blisscakes.pages

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.blisscakes.R
import com.example.blisscakes.navigation.NavRoutes
import com.example.blisscakes.ui.theme.BlissCakesTheme

// SplashScreen.kt
@Composable
fun SplashScreen(navController: NavHostController) {
    val config = LocalConfiguration.current
    val isTablet = config.screenWidthDp > 600
    val isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE

    BlissCakesTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LeftContent(
                        isTablet,
                        navToLogin = { navController.navigate(NavRoutes.Login) },
                        navToSignup = { navController.navigate(NavRoutes.Signup) }
                    )
                    ImageSection()
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    ImageSection()
                    Spacer(modifier = Modifier.height(16.dp))
                    LeftContent(
                        isTablet,
                        navToLogin = { navController.navigate(NavRoutes.Login) },
                        navToSignup = { navController.navigate(NavRoutes.Signup) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LeftContent(
    isTablet: Boolean,
    navToLogin: () -> Unit,
    navToSignup: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(if (isTablet) 0.6f else 1f)
    ) {
        Text(
            text = "Yummy sweets delivered to your dining table!",
            fontSize = if (isTablet) 40.sp else 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = if (isTablet) 52.sp else 36.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Freshly baked happiness, just a click away.\nCelebration starts with our cakes.",
            fontSize = if (isTablet) 18.sp else 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(
                onClick = navToLogin,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
            ) {
                Text("LOGIN")
            }
            Button(
                onClick = navToSignup,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("SIGNUP", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
private fun ImageSection() {
    Image(
        painter = painterResource(id = R.drawable.muffins),
        contentDescription = "Muffins",
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp, max = 300.dp)
    )
}
