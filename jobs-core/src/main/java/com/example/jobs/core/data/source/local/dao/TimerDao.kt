package com.example.jobs.core.data.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.jobs.core.data.source.local.entity.TimerEntity
import com.example.jobs.core.data.source.local.entity.TimerIntervalEntity

@Dao
abstract class TimerDao {

    @Insert
    protected abstract suspend fun createTimer(timerEntity: TimerEntity): Long

    @Insert
    protected abstract suspend fun createTimerIntervals(timerInterval: List<TimerIntervalEntity>): Long

    @Transaction
    open suspend fun createTimer(timerName: String, timerIntervals: List<TimerIntervalEntity>) {
        val timerId = createTimer(TimerEntity(name = timerName, updateOnInMillis = System.currentTimeMillis()))
        val intervalsWithTimerId = timerIntervals.map { it.copy(timerId = timerId) }
        createTimerIntervals(intervalsWithTimerId)
    }

    @Query("SELECT EXISTS(SELECT 1 FROM timers WHERE id = :timerId)")
    abstract suspend fun timerExist(timerId: Long): Boolean

    @Query("SELECT * FROM timer_intervals WHERE timer_id = :timerId")
    abstract suspend fun getTimerIntervals(timerId: Long): List<TimerIntervalEntity>

    @Transaction
    open suspend fun addIntervalToTimer(timerId: Long, timerIntervals: List<TimerIntervalEntity>): Boolean {
        if(!timerExist(timerId)) return false

        val intervalsWithTimerId = timerIntervals.map { it.copy(timerId = timerId) }
        createTimerIntervals(intervalsWithTimerId)
        updateTimerUpdateOnTime(timerId, System.currentTimeMillis())
        return true
    }

    @Query("UPDATE timers SET updated_on = :newUpdateOnTimeInMillis WHERE id = :timerId")
    abstract suspend fun updateTimerUpdateOnTime(timerId: Long, newUpdateOnTimeInMillis: Long)

    @Query("DELETE FROM timers WHERE id = :timerId")
    abstract suspend fun deleteTimer(timerId: Long): Int

    @Query("DELETE FROM timer_intervals WHERE id = :timerIntervalId")
    abstract suspend fun deleteTimerInterval(timerIntervalId: Long): Int

    @Query("""
        SELECT * FROM timers
        WHERE updated_on < :lastUpdatedOn
        ORDER BY updated_on DESC
        LIMIT :pageSize
    """)
    abstract suspend fun getPagedTimers(pageSize: Int, lastUpdatedOn: Long): List<TimerEntity>


    @Query("SELECT * FROM timer_intervals WHERE timer_id IN (:timerIds)")
    abstract suspend fun getTimerIntervalsForTimers(timerIds: List<Long>): List<TimerIntervalEntity>

}