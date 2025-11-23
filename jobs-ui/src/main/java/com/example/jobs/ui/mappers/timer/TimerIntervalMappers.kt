package com.example.jobs.ui.mappers.timer

import com.example.jobs.core.domain.model.TimerInterval
import com.example.jobs.ui.uimodels.timer.TimerIntervalUiModel

fun TimerInterval.toUiModel() = TimerIntervalUiModel(
    id = id,
    intervalInMins = intervalInMins
)

fun TimerIntervalUiModel.toDomainModel() = TimerInterval(
    id = id,
    intervalInMins = intervalInMins
)

fun List<TimerIntervalUiModel>.toDomainModel() = map { it.toDomainModel() }

fun List<TimerInterval>.toUiModel() = map { it.toUiModel() }