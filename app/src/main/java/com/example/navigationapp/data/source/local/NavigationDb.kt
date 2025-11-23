package com.example.navigationapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jobs.core.data.source.local.dao.TimerDao
import com.example.jobs.core.data.source.local.entity.TimerEntity
import com.example.jobs.core.data.source.local.entity.TimerIntervalEntity


@Database(
    entities = [
        NewsPostEntity::class,
        TimerEntity::class,
        TimerIntervalEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class NavigationDb: RoomDatabase() {
    abstract fun newsPostDao(): NewsPostDao

    abstract fun timerDao(): TimerDao
}