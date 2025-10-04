package com.example.jobs_core.data.model

data class TaskSubmissionModel(
    val title: String,
    val description: String,
    val place: String = "",
)
