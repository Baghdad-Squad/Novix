package com.baghdad.novix

import android.app.Application
import com.baghdad.novix.di.remoteDataSourceModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class NovixApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NovixApplication)
            modules(remoteDataSourceModule)
        }
    }
}