package com.example.jobs.core.domain.usecases

import com.example.common.core.utils.Result
import com.example.jobs.core.data.source.remote.model.TaskDataModel
import com.example.jobs.core.data.source.remote.model.TaskSubmissionModel
import com.example.jobs.core.domain.repositories.TasksRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskUseCase @Inject constructor(
    val taskRepository: TasksRepo
) {

    suspend fun loadTaskItems(pageNo: Int, pageSize: Int): List<TaskDataModel> {
        return taskRepository.getTasks(pageNo, pageSize)
    }

    suspend fun loadTask(taskId: Int): Flow<Result<TaskDataModel>> {
        return taskRepository.getTask(taskId)
    }

    suspend fun createTask(taskSubmissionModel: TaskSubmissionModel): Flow<Result<Unit>> {
        return taskRepository.createTask(taskSubmissionModel)
    }
}