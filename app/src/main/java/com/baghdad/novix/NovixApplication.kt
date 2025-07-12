package com.baghdad.novix

import android.app.Application
import com.baghdad.novix.di.localDataSourceModule
import com.baghdad.novix.di.remoteDataSourceModule
import com.baghdad.novix.di.repositoryModule
import com.baghdad.novix.di.useCaseModule
import com.baghdad.novix.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.Module

class NovixApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NovixApplication)
            modules(
                listOf<Module>(
                    localDataSourceModule,
                    remoteDataSourceModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}