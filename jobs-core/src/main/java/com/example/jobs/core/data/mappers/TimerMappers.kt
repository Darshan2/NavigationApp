package com.example.jobs.core.data.mappers

import com.example.jobs.core.data.source.local.entity.TimerEntity
import com.example.jobs.core.data.source.local.entity.TimerIntervalEntity
import com.example.jobs.core.domain.model.Timer
import com.example.jobs.core.domain.model.TimerInterval

fun TimerEntity.toDomainModel(): Timer {
    return Timer(
        id = id,
        name = name,
        updatedOnInMillis = updateOnInMillis
    )
}

fun TimerIntervalEntity.toDomainModel(): TimerInterval {
    return TimerInterval(
        id = id,
        intervalInMins = intervalInMins
    )
}

fun TimerInterval.toEntity(): TimerIntervalEntity {
    return TimerIntervalEntity(
        intervalInMins = intervalInMins
    )
}