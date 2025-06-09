package com.example.blisscakes.pages

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.blisscakes.R
import com.example.blisscakes.navigation.NavRoutes
import com.example.blisscakes.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    val config = LocalConfiguration.current
    val isTablet = config.screenWidthDp > 600
    val isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Page Transition
    var isVisible by remember { mutableStateOf(false) }
    var isExiting by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    val navigateToLogin = {
        isExiting = true
        navController.navigate(NavRoutes.Login)
    }

    val navigateToSignup = {
        isExiting = true
        navController.navigate(NavRoutes.Signup)
    }

    BlissCakesTheme {
        AnimatedVisibility(
            visible = isVisible && !isExiting,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            ) + fadeIn(
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            ) + fadeOut(
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    // Gradient background
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                LightPink,
                                SoftPink,
                                MutedRose
                            )
                        )
                    )
            ) {
                // App logo
                AnimatedVisibility(
                    visible = isVisible,
                    enter = scaleIn(
                        initialScale = 0.3f,
                        animationSpec = tween(
                            durationMillis = 1000,
                            delayMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(
                        animationSpec = tween(
                            durationMillis = 800,
                            delayMillis = 300
                        )
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 32.dp, end = 32.dp)
                            .size(if (isTablet) 80.dp else 70.dp)
                            .zIndex(2f)
                    )
                }

                // Layout adapts to orientation
                if (isLandscape) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 40.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LeftContent(
                            isTablet,
                            navToLogin = navigateToLogin,
                            navToSignup = navigateToSignup,
                            modifier = Modifier.weight(1f),
                            isVisible = isVisible
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 40.dp)
                                .offset(x = 40.dp)
                        ) {
                            ImageSection(isVisible = isVisible)
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(40.dp))
                        ImageSection(isVisible = isVisible)
                        Spacer(modifier = Modifier.height(32.dp))
                        LeftContent(
                            isTablet,
                            navToLogin = navigateToLogin,
                            navToSignup = navigateToSignup,
                            isVisible = isVisible
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LeftContent(
    isTablet: Boolean,
    navToLogin: () -> Unit,
    navToSignup: () -> Unit,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    val isDarkTheme = isSystemInDarkTheme()

    Column(
        modifier = modifier.fillMaxWidth(if (isTablet) 0.7f else 0.9f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(
                    durationMillis = 800,
                    delayMillis = 400,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = 800,
                    delayMillis = 400
                )
            )
        ) {
            Text(
                text = "Yummy sweets delivered to your dining table!",
                fontSize = if (isTablet) 36.sp else 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = if (isTablet) 48.sp else 34.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(
                    durationMillis = 800,
                    delayMillis = 600,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = 800,
                    delayMillis = 600
                )
            )
        ) {
            Text(
                text = "Freshly baked happiness, just a click away.\nCelebration starts with our cakes.",
                fontSize = if (isTablet) 18.sp else 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Animated buttons
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(
                    durationMillis = 800,
                    delayMillis = 800,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = 800,
                    delayMillis = 800
                )
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LOGIN button with hover effect
                var loginPressed by remember { mutableStateOf(false) }
                val loginScale by animateFloatAsState(
                    targetValue = if (loginPressed) 0.95f else 1f,
                    animationSpec = tween(100),
                    label = "loginScale"
                )

                OutlinedButton(
                    onClick = navToLogin,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (isDarkTheme) TextLight else PinkPrimary
                    ),
                    border = BorderStroke(
                        width = 2.dp,
                        color = if (isDarkTheme) TextLight else PinkPrimary
                    ),
                    modifier = Modifier
                        .width(140.dp)
                        .graphicsLayer(scaleX = loginScale, scaleY = loginScale)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    loginPressed = true
                                    tryAwaitRelease()
                                    loginPressed = false
                                }
                            )
                        }
                ) {
                    Text("LOGIN", fontWeight = FontWeight.SemiBold)
                }

                // SIGNUP button with hover effect
                var signupPressed by remember { mutableStateOf(false) }
                val signupScale by animateFloatAsState(
                    targetValue = if (signupPressed) 0.95f else 1f,
                    animationSpec = tween(100),
                    label = "signupScale"
                )

                Button(
                    onClick = navToSignup,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PinkPrimary,
                        contentColor = White
                    ),
                    modifier = Modifier
                        .width(140.dp)
                        .graphicsLayer(scaleX = signupScale, scaleY = signupScale)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    signupPressed = true
                                    tryAwaitRelease()
                                    signupPressed = false
                                }
                            )
                        }
                ) {
                    Text(
                        "SIGNUP",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun ImageSection(isVisible: Boolean = true) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Landing image
        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(
                    durationMillis = 1000,
                    delayMillis = 200,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = 800,
                    delayMillis = 200
                )
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.landing_image),
                contentDescription = "Wedding Image",
                modifier = Modifier
                    .heightIn(min = 260.dp, max = 360.dp)
                    .fillMaxWidth(0.9f)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(24.dp),
                        clip = true,
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}