package com.example.common.core.utils


fun toMins(hours: Int, mins: Int): Int {
    require(hours >= 0 && hours < 24) { "Invalid hours: $hours" }
    require(mins >= 0 && mins < 60) { "Invalid minutes: $mins" }

    return hours * 60 + mins
}
