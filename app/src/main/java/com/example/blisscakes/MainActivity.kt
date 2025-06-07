package com.example.blisscakes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.blisscakes.ui.theme.BlissCakesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlissCakesTheme {
                MyApplicationNavigation()
            }
        }
    }
}
