package com.example.jobs.core.data.source

import com.example.jobs.core.data.source.remote.model.TaskDataModel
import com.example.jobs.core.data.source.remote.model.TaskSubmissionModel


interface TasksDataSource {
    suspend fun loadTasks(pageNo: Int, pageSize: Int): List<TaskDataModel>?
    suspend fun loadTask(taskId: Int): TaskDataModel?
    suspend fun createTask(taskSubmissionModel: TaskSubmissionModel)

}