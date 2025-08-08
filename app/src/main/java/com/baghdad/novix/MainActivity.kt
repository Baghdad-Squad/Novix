package com.baghdad.novix

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.main.MainScreen
import com.baghdad.viewmodel.main.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state by mainViewModel.uiState.collectAsStateWithLifecycle()
            val isDarkTheme = state.isAppInDarkTheme

            NovixTheme(isDarkTheme = isDarkTheme) {
                val systemUiController = rememberSystemUiController()
                val surfaceColor = Theme.color.surface

                LaunchedEffect(isDarkTheme) {
                    if (isDarkTheme != null) {
                        systemUiController.setSystemBarsColor(
                            color = surfaceColor,
                            darkIcons = !isDarkTheme
                        )
                    }
                }

                MainScreen(state = state)
            }
        }
    }
}