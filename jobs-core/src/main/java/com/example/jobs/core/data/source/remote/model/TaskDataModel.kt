package com.example.jobs.core.data.source.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskDataModel(
    val id: Int,
    val title: String,
    val description: String = "",
    val place: String = ""
): Parcelable