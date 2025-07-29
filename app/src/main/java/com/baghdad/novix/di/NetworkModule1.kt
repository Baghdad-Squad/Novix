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
            return "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyZTZkYmRkOTNjMGY5NzdkMjMwOGJjMzM3NmI3YTNmOCIsIm5iZiI6MTc1MzAyOTE3Ni45OSwic3ViIjoiNjg3ZDFhMzgzOTg0OWZmZThkZDk4ZDEzIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.5NDfRH9_oRVtrvQb8Bs11qWGeLzEE5US_e5IcVQWerE"
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
                .baseUrl("https://api.themoviedb.org/3/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Named("BASE_URL")
        fun provideBaseUrl(): String {
            return "https://api.themoviedb.org/3"
        }

        @Provides
        @Named("API_KEY")
        fun provideApiKey(): String {
            return BuildConfig.API_KEY
        }
    }
}