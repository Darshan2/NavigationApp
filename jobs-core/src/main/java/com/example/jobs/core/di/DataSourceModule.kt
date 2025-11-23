package com.example.jobs.core.di

import com.example.jobs.core.data.repositoryimpls.TasksRepoImpl
import com.example.jobs.core.data.repositoryimpls.TimerRepoImpl
import com.example.jobs.core.data.source.TasksDataSource
import com.example.jobs.core.data.source.remote.TaskRemoteDataSource
import com.example.jobs.core.domain.repositories.TasksRepo
import com.example.jobs.core.domain.repositories.TimerRepo
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

    @Binds
    internal abstract fun bindsTimerRepo(
        timerRepoImpl: TimerRepoImpl
    ): TimerRepo
}