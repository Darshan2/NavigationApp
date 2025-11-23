package com.example.jobs.ui.mappers.timer

import com.example.jobs.core.domain.model.Timer
import com.example.jobs.ui.uimodels.timer.TimerUiModel

fun Timer.toUiModel() = TimerUiModel(
    id = id,
    name = name,
    timerIntervals = timerIntervals.toUiModel()
)

fun TimerUiModel.toDomainModel() = Timer(
    id = id,
    name = name,
    timerIntervals = timerIntervals.toDomainModel()
)

fun List<Timer>.toUiModel() = map { it.toUiModel() }

fun List<TimerUiModel>.toDomainModel() = map { it.toDomainModel() }
