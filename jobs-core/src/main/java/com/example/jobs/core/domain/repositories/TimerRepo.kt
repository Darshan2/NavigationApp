package com.example.jobs.core.domain.repositories

import androidx.paging.PagingData
import com.example.common.core.utils.Result
import com.example.jobs.core.domain.model.Timer
import com.example.jobs.core.domain.model.TimerInterval
import kotlinx.coroutines.flow.Flow

interface TimerRepo {
    suspend fun createTimer(timerName: String, intervals: List<TimerInterval>?): Flow<Result<Unit>>
    suspend fun deleteTimer(timerId: Long): Flow<Result<Boolean>>

    suspend fun getTimerIntervals(timerId: Long): Flow<Result<List<TimerInterval>>>
    suspend fun addTimerInterval(timerId: Long, intervals: List<TimerInterval>?): Flow<Result<Boolean>>
    suspend fun deleteTimerInterval(timerIntervalId: Long): Flow<Result<Boolean>>
}