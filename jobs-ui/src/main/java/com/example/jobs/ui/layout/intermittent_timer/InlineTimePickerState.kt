package com.example.jobs.ui.layout.intermittent_timer

class InlineTimePickerState(
    initialHour: Int,
    initialMinute: Int,
    val is24Hour: Boolean = false
) {
    init {
        require(initialHour in 0..23) { "initialHour should in [0..23] range" }
        require(initialMinute in 0..59) { "initialMinute should be in [0..59] range" }
    }

    var selectedMinute: Int = initialMinute
        set(value) {
            field = value.coerceIn(0..59)
        }

    var selectedHour: Int = initialHour
        set(value) {
            field = if(is24Hour)
                value.coerceIn(0..23)
            else
                value.coerceIn(1..12)
        }

}