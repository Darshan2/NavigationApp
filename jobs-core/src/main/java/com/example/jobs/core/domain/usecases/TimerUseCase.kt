package com.example.jobs.core.domain.usecases

import com.example.common.core.utils.Result
import com.example.jobs.core.domain.model.Timer
import com.example.jobs.core.domain.model.TimerInterval
import com.example.jobs.core.domain.repositories.TimerRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TimerUseCase @Inject constructor(
    private val timerRepo: TimerRepo
) {

    suspend fun createTimer(timerName: String, intervals: List<TimerInterval>?): Flow<Result<Unit>> {
        return timerRepo.createTimer(timerName, intervals)
    }

    suspend fun deleteTimer(timerId: Long): Flow<Result<Boolean>> {
        return timerRepo.deleteTimer(timerId)
    }

    suspend fun getTimerIntervals(timerId: Long): Flow<Result<List<TimerInterval>>> {
        return timerRepo.getTimerIntervals(timerId)
    }

    suspend fun addTimerInterval(timerId: Long, intervals: List<TimerInterval>?): Flow<Result<Boolean>> {
        return timerRepo.addTimerInterval(timerId, intervals)
    }

    suspend fun deleteTimerInterval(timerIntervalId: Long): Flow<Result<Boolean>> {
        return timerRepo.deleteTimerInterval(timerIntervalId)
    }
}