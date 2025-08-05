package com.baghdad.novix

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.ui.main.MainScreen
import com.baghdad.viewmodel.util.LanguageManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val newLanguage = newConfig.locales[0].language
        Log.i("LanguageManager", "Current language: ${LanguageManager.currentLanguage.value}")
        Log.i("LanguageManager", "system language: $newLanguage")
        if (newLanguage != LanguageManager.currentLanguage.value) {
            LanguageManager.updateLanguage(newLanguage)
        }
    }
}
