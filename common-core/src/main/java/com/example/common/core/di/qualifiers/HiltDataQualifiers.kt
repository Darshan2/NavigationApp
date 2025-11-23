package com.example.common.core.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewsLocalDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewsRemoteDataSource
