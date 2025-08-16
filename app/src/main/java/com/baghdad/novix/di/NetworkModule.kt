package com.baghdad.novix.di

import android.content.Context
import com.baghdad.novix.BuildConfig
import com.baghdad.remoteDataSource.interceptor.AuthenticationInterceptor
import com.baghdad.remoteDataSource.interceptor.CacheInterceptor
import com.baghdad.remoteDataSource.interceptor.LanguageInterceptor
import com.baghdad.repository.datasource.local.AppConfigurationDataSource
import com.baghdad.repository.datasource.local.SessionDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Named
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    companion object {
        private const val TIME_OUT = 20L
        private const val CACHE_SIZE_MB = 10


        @Provides
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }


        @Provides
        @Named("AUTHORIZATION_TOKEN")
        fun provideAuthorizationToken(): String {
            return BuildConfig.AUTHORIZATION_TOKEN
        }

        @Provides
        fun provideAuthenticationInterceptor(
            sessionDataSource: SessionDataSource,
            @Named("AUTHORIZATION_TOKEN") authorizationToken: String,
        ): AuthenticationInterceptor =
            AuthenticationInterceptor(authorizationToken, sessionDataSource)

        @Provides
        fun provideLanguageInterceptor(
            appConfigurationDataSource: AppConfigurationDataSource
        ): LanguageInterceptor {
            return LanguageInterceptor(appConfigurationDataSource)
        }

        @Provides
        fun provideCacheInterceptor(): CacheInterceptor {
            return CacheInterceptor()
        }

        @Provides
        fun provideCache(@ApplicationContext context: Context): Cache {
            val maximumCacheSize = CACHE_SIZE_MB * 1024 * 1024
            return Cache(File(context.cacheDir, "http-cache"), maximumCacheSize.toLong())
        }


        @Provides
        fun provideOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            authenticationInterceptor: AuthenticationInterceptor,
            languageInterceptor: LanguageInterceptor,
            cacheInterceptor: CacheInterceptor,
            cache: Cache
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .callTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .cache(cache)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authenticationInterceptor)
                .addInterceptor(languageInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
                .build()
        }

        @Provides
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Named("BASE_URL")
        fun provideBaseUrl(): String {
            return BuildConfig.BASE_URL
        }

        @Provides
        @Named("API_KEY")
        fun provideApiKey(): String {
            return BuildConfig.API_KEY
        }
    }
}