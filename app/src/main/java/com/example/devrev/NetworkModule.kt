package com.example.devrev

import com.devrevsdk.GsonConverterFactory
import com.devrevsdk.NetworkClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .setPrettyPrinting()
            .create()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory(gson)
    }

    @Provides
    @Singleton
    fun provideNetworkClient(converterFactory: GsonConverterFactory): NetworkClient {
        return NetworkClient.Builder()
            .setBaseUrl("http://api.themoviedb.org")
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")//Content-Type: text/html
            .addHeader("User-Agent", userAgent)
            .enableLogging(true)
            .setConverterFactory(converterFactory)
            .build()
    }
    val userAgent by lazy {
        System.getProperty("http.agent") ?: ""
    }
}
