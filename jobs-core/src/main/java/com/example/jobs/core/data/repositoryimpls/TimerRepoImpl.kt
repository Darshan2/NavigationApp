package com.example.jobs.core.data.repositoryimpls

import com.example.common.core.utils.Result
import com.example.common.core.utils.safeApiCall
import com.example.jobs.core.data.source.local.TimerLocalDataSource
import com.example.jobs.core.domain.model.Timer
import com.example.jobs.core.domain.model.TimerInterval
import com.example.jobs.core.domain.repositories.TimerRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class TimerRepoImpl @Inject constructor(
    private val timerLocalDataSource: TimerLocalDataSource
): TimerRepo {

    override suspend fun createTimer(timerName: String, intervals: List<TimerInterval>?): Flow<Result<Unit>> {
        if(intervals.isNullOrEmpty()) {
            return flowOf(Result.Error(Exception("Time Intervals cannot be empty")))
        }

        return safeApiCall {
            timerLocalDataSource.createTimer(timerName, intervals)
        }
    }


    override suspend fun deleteTimer(timerId: Long): Flow<Result<Boolean>> {
        return safeApiCall(
            call = { timerLocalDataSource.deleteTimer(timerId) },
            errorOnResult = { !it },
            errorOnResultMessage = "Timer with id:$timerId not found"
        )
    }

    override suspend fun getTimerIntervals(timerId: Long): Flow<Result<List<TimerInterval>>> {
        return safeApiCall {
            timerLocalDataSource.getTimerIntervals(timerId)
        }
    }

    override suspend fun addTimerInterval(timerId: Long, intervals: List<TimerInterval>?): Flow<Result<Boolean>> {
        if(intervals.isNullOrEmpty()) {
            return flowOf(Result.Error(Exception("Timer Intervals cannot be empty")))
        }

        return safeApiCall(
            call = { timerLocalDataSource.addTimerInterval(timerId, intervals) },
            errorOnResult = { !it },
            errorOnResultMessage = "Timer with id:$timerId not found"
        )
    }

    override suspend fun deleteTimerInterval(timerIntervalId: Long): Flow<Result<Boolean>> {
        return safeApiCall {
            timerLocalDataSource.deleteTimerInterval(timerIntervalId)
        }
    }
}