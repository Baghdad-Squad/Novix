package com.baghdad.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.theme.NovixTheme
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
            MainContent(mainViewModel = mainViewModel)
        }
    }

    @Composable
    private fun MainContent(mainViewModel: MainViewModel) {
        val state by mainViewModel.uiState.collectAsStateWithLifecycle()
        val isDarkTheme = state.isAppInDarkTheme
        var isStatusBarTransparent by remember {
            mutableStateOf(false)
        }

        NovixTheme(isDarkTheme = isDarkTheme) {
            ConfigureSystemBars(
                isDarkTheme = isDarkTheme,
                isStatusBarTransparent = isStatusBarTransparent
            )
            MainScreen(
                state = state,
                onStatusBarTransparencyChanged = { transparent ->
                    isStatusBarTransparent = transparent
                }
            )
        }
    }

    @Composable
    private fun ConfigureSystemBars(
        isDarkTheme: Boolean?,
        isStatusBarTransparent: Boolean
    ) {
        val systemUiController = rememberSystemUiController()

        LaunchedEffect(isDarkTheme, isStatusBarTransparent) {
            isDarkTheme?.let { darkTheme ->
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = !darkTheme
                )
            }
        }
    }
}