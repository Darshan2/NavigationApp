package com.example.jobs.core.data.repositoryimpls

import com.example.common.core.utils.Result
import com.example.common.core.utils.safeApiCall
import com.example.jobs.core.data.source.remote.model.TaskDataModel
import com.example.jobs.core.data.source.remote.model.TaskSubmissionModel
import com.example.jobs.core.data.source.TasksDataSource
import com.example.jobs.core.di.TasksRemoteDataSrc
import com.example.jobs.core.domain.repositories.TasksRepo
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