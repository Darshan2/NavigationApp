package com.example.jobs_core.data.repositoryImpls

import com.example.common_core.utils.Result
import com.example.common_core.utils.safeApiCall
import com.example.jobs_core.data.model.TaskDataModel
import com.example.jobs_core.data.model.TaskSubmissionModel
import com.example.jobs_core.data.source.TasksDataSource
import com.example.jobs_core.di.TasksRemoteDataSrc
import com.example.jobs_core.domain.repositories.TasksRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TasksRepoImpl @Inject constructor(
    @TasksRemoteDataSrc private val tasksDataSource: TasksDataSource
): TasksRepo {

    override suspend fun getTasks(pageNo: Int, pageSize: Int): List<TaskDataModel> {
        return tasksDataSource.loadTasks(pageNo, pageSize) ?: mutableListOf()
    }

    override suspend fun getTask(taskId: Int): Flow<Result<TaskDataModel>> {
        return safeApiCall {
            tasksDataSource.loadTask(taskId) ?: throw Exception("Task not found")
        }
    }

    override suspend fun createTask(taskSubmissionModel: TaskSubmissionModel): Flow<Result<Unit>> {
        return safeApiCall {
            tasksDataSource.createTask(taskSubmissionModel)
        }
    }
}