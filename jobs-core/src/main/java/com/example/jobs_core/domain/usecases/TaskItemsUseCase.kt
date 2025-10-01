package com.example.jobs_core.domain.usecases

import com.example.common_core.utils.Result
import com.example.jobs_core.data.model.TaskDataModel
import com.example.jobs_core.domain.repositories.TasksRepo
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
}