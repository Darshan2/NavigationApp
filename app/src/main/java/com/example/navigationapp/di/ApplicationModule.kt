package com.example.navigationapp.di

import android.content.Context
import androidx.room.Room
import com.example.common_core.di.qualifiers.DefaultDispatcher
import com.example.common_core.di.qualifiers.IoDispatcher
import com.example.common_core.di.qualifiers.MainDispatcher
import com.example.navigationapp.data.source.local.NavigationDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideNavigationDb(@ApplicationContext app: Context): NavigationDb {
        return Room.databaseBuilder(
            context = app,
            klass = NavigationDb::class.java,
            name = "Navigation.db"
        ).build()
    }

    @Provides
    fun provideNewsPostDao(db: NavigationDb) = db.newsPostDao()
}




