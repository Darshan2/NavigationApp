package com.example.jobs.ui.uimodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.jobs.core.data.source.remote.model.TaskDataModel

data class TaskUiModel(
    val id: Int,
    val title: String,
    val description: String,
    val selected: MutableState<Boolean> = mutableStateOf(false),
    val isMarkedForDelete: MutableState<Boolean> = mutableStateOf(false)
)

fun TaskDataModel.toUiModel(): TaskUiModel {
    return TaskUiModel(
        id = this.id,
        title = this.title,
        description = this.description
    )
}

fun TaskUiModel.toDataModel(): TaskDataModel {
    return TaskDataModel(
        id = this.id,
        title = this.title,
        description = this.description
    )
}

fun List<TaskDataModel>.toUiModel(): List<TaskUiModel> {
    return this.map { it.toUiModel() }
}