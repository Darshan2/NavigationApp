package com.example.jobs.core.domain.model

data class Timer(
    val id: Long,
    val name: String,
    val updatedOnInMillis: Long,
    val timerIntervals: List<TimerInterval> = emptyList()
)

data class TimerInterval(
    val id: Long = 0,
    val intervalInMins: Int
)
