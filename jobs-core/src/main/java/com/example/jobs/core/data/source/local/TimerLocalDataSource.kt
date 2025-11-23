package com.example.jobs.core.data.source.local

import com.example.jobs.core.data.mappers.toDomainModel
import com.example.jobs.core.data.mappers.toEntity
import com.example.jobs.core.data.source.local.dao.TimerDao
import com.example.jobs.core.domain.model.Timer
import com.example.jobs.core.domain.model.TimerInterval
import javax.inject.Inject

class TimerLocalDataSource @Inject constructor (
    private val timerDao: TimerDao
){
    suspend fun createTimer(timerName: String, intervals: List<TimerInterval>) {
        val intervals = intervals.map { it.toEntity() }
        timerDao.createTimer(timerName, intervals)
    }

    suspend fun deleteTimer(timerId: Long): Boolean {
        return timerDao.deleteTimer(timerId) > 0
    }

    suspend fun getTimerIntervals(timerId: Long): List<TimerInterval> {
        return timerDao.getTimerIntervals(timerId).map { it.toDomainModel() }
    }

    /**
     * Adds one or more timer intervals to the specified timer.
     *
     * This function converts the given domain-level [TimerInterval] objects into their
     * corresponding database entities and delegates the insertion to [TimerDao.addIntervalToTimer].
     *
     * @param timerId The unique ID of the timer to which the intervals belong.
     * @param intervals A list of [TimerInterval] domain models to be added for the timer.
     *
     * @return `true` if the intervals were successfully added, `false` otherwise.
     *
     * @see TimerDao.addIntervalToTimer
     */
    suspend fun addTimerInterval(timerId: Long, intervals: List<TimerInterval>): Boolean {
        return timerDao.addIntervalToTimer(
            timerId = timerId,
            timerIntervals = intervals.map { it.toEntity() }
        )
    }

    suspend fun deleteTimerInterval(timerIntervalId: Long): Boolean {
        return timerDao.deleteTimerInterval(timerIntervalId) > 0
    }
}