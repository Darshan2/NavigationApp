package com.example.navigationapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [NewsPostEntity::class], version = 1, exportSchema = true)
abstract class NavigationDb: RoomDatabase() {
    abstract fun newsPostDao(): NewsPostDao
}