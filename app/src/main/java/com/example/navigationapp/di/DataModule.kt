package com.example.navigationapp.di

import com.example.common_core.di.qualifiers.NewsLocalDataSource
import com.example.common_core.di.qualifiers.NewsRemoteDataSource
import com.example.navigationapp.data.repository.NewsBaseRepository
import com.example.navigationapp.data.repository.NewsRepository
import com.example.navigationapp.data.source.NewsDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @NewsRemoteDataSource
    abstract fun bindsNewsRemoteDataSource(
        newsRemoteDataSource: com.example.navigationapp.data.source.remote.NewsRemoteDataSource
    ): NewsDataSource

    @Binds
    @NewsLocalDataSource
    abstract fun bindsNewsLocalDataSource(
        newsLocalDataSource: com.example.navigationapp.data.source.local.NewsLocalDataSource
    ): NewsDataSource

    @Binds
    abstract fun bindsNewsRepository(
        newsRepository: NewsRepository
    ): NewsBaseRepository
}