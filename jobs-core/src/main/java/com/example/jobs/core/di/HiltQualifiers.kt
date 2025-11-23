package com.example.jobs.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TasksRemoteDataSrc

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TasksLocalDataSrc
