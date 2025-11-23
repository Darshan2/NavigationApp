package com.example.jobs.ui.events

import com.example.jobs.ui.uimodels.TaskUiModel

interface TaskEvents {
    fun updateTask(selected: Boolean, task: TaskUiModel)
    fun removeTask(task: TaskUiModel)
}

fun defaultTaskEvents() = object : TaskEvents {
    override fun updateTask(selected: Boolean, task: TaskUiModel) {}
    override fun removeTask(task: TaskUiModel) {}
}