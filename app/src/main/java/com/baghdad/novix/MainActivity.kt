package com.baghdad.novix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.ui.feature.trendingActors.TrendingActorsScreen
import com.baghdad.ui.main.MainScreen
import com.baghdad.ui.navigation.NovixNavHost
import com.baghdad.ui.navigation.graph.home.HomeNavEvent
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.viewmodel.people.TrendingActorViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NovixTheme {
                MainScreen()
            }
        }
    }
}
