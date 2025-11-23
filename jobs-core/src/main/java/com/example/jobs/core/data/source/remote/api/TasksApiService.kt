package com.example.jobs.core.data.source.remote.api

import com.example.jobs.data.model.TaskDataModel
import retrofit2.http.GET

interface TasksApiService {

    @GET
    suspend fun loadTasks(pageNo: Int, pageSize: Int): List<TaskDataModel>?

    @GET
    suspend fun loadTask(taskId: Int): TaskDataModel?

}