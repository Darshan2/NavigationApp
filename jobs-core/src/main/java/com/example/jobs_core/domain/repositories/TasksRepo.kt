package com.example.jobs_core.domain.repositories

import com.example.common_core.utils.Result
import com.example.jobs_core.data.model.TaskDataModel
import com.example.jobs_core.data.model.TaskSubmissionModel
import kotlinx.coroutines.flow.Flow

interface TasksRepo {
    suspend fun getTasks(pageNo: Int, pageSize: Int): List<TaskDataModel>
    suspend fun getTask(taskId: Int): Flow<Result<TaskDataModel>>
    suspend fun createTask(taskSubmissionModel: TaskSubmissionModel): Flow<Result<Unit>>
}