package com.example.jobs.core.data.repositoryimpls

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.jobs.core.data.source.paging.TimerPagingSource
import com.example.jobs.core.domain.model.Timer
import com.example.jobs.core.domain.model.TimerInterval
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TimerPaginator @Inject constructor(
    private val timerPagingSource: TimerPagingSource
) {
    fun createPaginator(pagingConfig: PagingConfig): Flow<PagingData<Timer>> {
        return Pager(config = pagingConfig) {
            timerPagingSource
        }.flow
    }
}