package com.example.jobs_core.data.source.remote.apis

import com.example.jobs_core.data.model.TaskDataModel
import retrofit2.http.GET

interface TasksApiService {

    @GET
    suspend fun loadTasks(pageNo: Int, pageSize: Int): List<TaskDataModel>?

    @GET
    suspend fun loadTask(taskId: Int): TaskDataModel?

}