package com.example.common.core.di.qualifiers

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher


//-----------------------------------------------------------------

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewsRetrofitClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class JobsRetrofitClient
