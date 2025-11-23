package com.example.jobs.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "timers")
data class TimerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("updated_on") val updateOnInMillis: Long,
)

@Entity(
    tableName = "timer_intervals",
    foreignKeys = [
        ForeignKey(
            entity = TimerEntity::class,
            parentColumns = ["id"],
            childColumns = ["timer_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("group_id")]
)
data class TimerIntervalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("timer_id") val timerId: Long = 0,
    @ColumnInfo("interval_in_mins") val intervalInMins: Int
)