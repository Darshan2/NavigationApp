package com.example.navigationapp.di

import android.content.Context
import androidx.room.Room
import com.example.jobs.core.data.source.local.dao.TimerDao
import com.example.navigationapp.data.source.local.NavigationDb
import com.example.navigationapp.data.source.local.NewsPostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

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
    fun provideNewsPostDao(db: NavigationDb): NewsPostDao {
        return db.newsPostDao()
    }

    @Provides
    fun provideTimerDao(db: NavigationDb): TimerDao {
        return db.timerDao()
    }
}




