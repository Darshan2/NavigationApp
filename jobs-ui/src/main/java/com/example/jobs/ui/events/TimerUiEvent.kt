package com.example.jobs.ui.events

sealed class TimerUiEvent {
    object OnTimerIntervalDeleteFailed: TimerUiEvent()
    object OnTimerDeleteFailed: TimerUiEvent()
    object OnTimerSaveFailed: TimerUiEvent()
}
