package com.example.jobs_core.data.source

import com.example.jobs_core.data.model.TaskDataModel

interface TasksDataSource {
    suspend fun loadTasks(pageNo: Int, pageSize: Int): List<TaskDataModel>?
    suspend fun loadTask(taskId: Int): TaskDataModel?
}