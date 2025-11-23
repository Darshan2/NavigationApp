package com.example.jobs.core.data.source.remote.model

data class TaskSubmissionModel(
    val title: String,
    val description: String,
    val place: String = "",
)
