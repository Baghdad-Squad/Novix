package com.baghdad.novix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.ui.feature.episodeDetails.EpisodeDetailsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NovixTheme {
//                val navController = rememberNavController()
//                NovixNavHost(
//                    navController = navController,
//                    startDestination = Graph.SearchGraph
//                )
                EpisodeDetailsScreen(
                    tvShowId = 1668,
                    seasonNumber = 1,
                    episodeNumber = 1
                ) { }
            }
        }
    }
}
