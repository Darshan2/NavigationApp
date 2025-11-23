package com.example.navigationapp.di

import com.example.common.core.di.qualifiers.NewsRetrofitClient
import com.example.navigationapp.data.source.remote.NewsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

object BaseUrls {
    const val NEWS_BASE_URL = "https://getlokalapp.com/"
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    @NewsRetrofitClient
    fun provideNewsRetrofitClient(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BaseUrls.NEWS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsApiService(@NewsRetrofitClient retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }
}