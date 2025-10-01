package com.example.jobs_core.di

import com.example.jobs_core.data.repositoryImpls.TasksRepoImpl
import com.example.jobs_core.data.source.TasksDataSource
import com.example.jobs_core.data.source.remote.TaskRemoteDataSource
import com.example.jobs_core.domain.repositories.TasksRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @TasksRemoteDataSrc
    internal abstract fun bindsTasksRemoteDataSource(
        tasksRemoteDataSource: TaskRemoteDataSource
    ): TasksDataSource

    @Binds
    internal abstract fun bindsTasksRepo(
        tasksRepoImpl: TasksRepoImpl
    ): TasksRepo
}