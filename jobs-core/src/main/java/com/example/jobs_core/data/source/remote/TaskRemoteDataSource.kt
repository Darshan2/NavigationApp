package com.example.jobs_core.data.source.remote

import com.example.jobs_core.data.model.TaskDataModel
import com.example.jobs_core.data.source.TasksDataSource
import com.example.jobs_core.data.source.remote.apis.TasksApiService
import kotlinx.coroutines.delay
import javax.inject.Inject

internal class TaskRemoteDataSource @Inject constructor(
    private val tasksApiService: TasksApiService
): TasksDataSource {

    override suspend fun loadTasks(pageNo: Int, pageSize: Int): List<TaskDataModel>? {
        if(pageNo < 0 || pageSize <= 0) {
            throw Exception("Invalid page number: $pageNo or page size: $pageSize")
        }

        //Todo: remove hardcoded values and delay
        delay(3000)
        val mutableList = mutableListOf<TaskDataModel>()

        val start = (pageNo * pageSize) + 1
        val end = start + pageSize
        for (i in start until end) {
            mutableList.add(TaskDataModel(i, "Task $i", "Description $i"))
        }
        return mutableList

//        return tasksApiService.loadTasks(pageNo, pageSize)
    }

    override suspend fun loadTask(taskId: Int): TaskDataModel? {
        if(taskId <= 0) {
            throw Exception("Invalid task id: $taskId")
        }

        //Todo: Remove delay and hard-coded return
        delay(3000)
        return TaskDataModel(taskId, "Task $taskId", "Description $taskId")

//        return tasksApiService.loadTask(taskId)
    }

}