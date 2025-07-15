package com.baghdad.novix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.novix.util.IconSwitcher
import com.baghdad.ui.feature.search.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IconSwitcher.switchAppIcon(this)
        enableEdgeToEdge()
        setContent {
            NovixTheme {
                SearchScreen(
                    navigateToActorDetails = { /* TODO: Implement navigation */ },
                    navigateToMovieDetails = { /* TODO: Implement navigation */ },
                    navigateToTvShowDetails = { /* TODO: Implement navigation */ },
                    navigateToRecentlyViewedDetails = { /* TODO: Implement navigation */ }
                )
            }
        }
    }
}
