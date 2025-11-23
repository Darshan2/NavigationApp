package com.example.jobs.core.di

import com.example.common.core.di.qualifiers.JobsRetrofitClient
import com.example.jobs.core.data.source.remote.api.TasksApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

object BaseUrls {
    const val JOBS_BASE_URL = "https://getlokaljob.com/"
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    @JobsRetrofitClient
    fun provideJobsRetrofitClient(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BaseUrls.JOBS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsApiService(@JobsRetrofitClient retrofit: Retrofit): TasksApiService {
        return retrofit.create(TasksApiService::class.java)
    }
}