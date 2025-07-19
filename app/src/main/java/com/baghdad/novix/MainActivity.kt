package com.baghdad.novix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.ui.navigation.NovixNavHost
import com.baghdad.ui.navigation.route.Graph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NovixTheme {
                val navController = rememberNavController()
                NovixNavHost(
                    navController = navController,
                    startDestination = Graph.SearchGraph
                )
            }
        }
    }
}
