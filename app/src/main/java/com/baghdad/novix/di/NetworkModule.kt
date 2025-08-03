package com.baghdad.novix.di

import com.baghdad.local_datasource.language.AppLanguageProvider
import com.baghdad.novix.BuildConfig
import com.baghdad.remoteDataSource.interceptor.HeadersSetupInterceptor
import com.baghdad.repository.language.LanguageProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Named
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    abstract fun provideLanguageProvider(imp : AppLanguageProvider): LanguageProvider

    companion object {
        private const val timeOut = 20L

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
        fun provideHeadersSetupInterceptor(
            languageProvider: LanguageProvider,
            @Named("AUTHORIZATION_TOKEN") authorizationToken: String
        ): HeadersSetupInterceptor {
            return HeadersSetupInterceptor(languageProvider, authorizationToken)
        }

        @Provides
        fun provideOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            headersSetupInterceptor: HeadersSetupInterceptor
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .callTimeout(timeOut, TimeUnit.SECONDS)
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headersSetupInterceptor)
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
