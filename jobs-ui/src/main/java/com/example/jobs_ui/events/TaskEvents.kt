package com.example.jobs_ui.events

import com.example.jobs_ui.ui_models.TaskUiModel

interface TaskEvents {
    fun updateTask(selected: Boolean, task: TaskUiModel)
    fun removeTask(task: TaskUiModel)
}

fun defaultTaskEvents() = object : TaskEvents {
    override fun updateTask(selected: Boolean, task: TaskUiModel) {}
    override fun removeTask(task: TaskUiModel) {}
}