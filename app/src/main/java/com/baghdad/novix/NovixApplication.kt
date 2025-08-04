package com.baghdad.novix

import android.app.Application
import com.baghdad.islamic_image_loader.ui.CustomImageLoader
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class NovixApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CustomImageLoader.init(this@NovixApplication)
    }
}