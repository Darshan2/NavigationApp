package com.example.jobs.core.domain.repositories

import com.example.common.core.utils.Result
import com.example.jobs.core.data.source.remote.model.TaskDataModel
import com.example.jobs.core.data.source.remote.model.TaskSubmissionModel
import kotlinx.coroutines.flow.Flow

interface TasksRepo {
    suspend fun getTasks(pageNo: Int, pageSize: Int): List<TaskDataModel>
    suspend fun getTask(taskId: Int): Flow<Result<TaskDataModel>>
    suspend fun createTask(taskSubmissionModel: TaskSubmissionModel): Flow<Result<Unit>>
}