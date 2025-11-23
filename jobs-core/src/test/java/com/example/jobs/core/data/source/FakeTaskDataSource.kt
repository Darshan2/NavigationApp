package com.example.jobs.core.data.source

import com.example.jobs.data.model.TaskDataModel
import com.example.jobs.data.model.TaskSubmissionModel
import com.example.jobs.data.source.TasksDataSource


class FakeTasksDataSourceBuilder {
    private var tasks: List<TaskDataModel>? = null
    private var mTask: TaskDataModel? = null
    private var errorRepose = false
    private var nullResponse = false
    private var emptyRepose = false

    fun withTasks(list: List<TaskDataModel>) = apply { tasks = list }
    fun withTask(task: TaskDataModel) = apply { mTask = task }
    fun returnErrorRepose() = apply { errorRepose = true }
    fun returnNullRepose() = apply { nullResponse = true }
    fun returnEmptyRepose() = apply { emptyRepose = true }

    internal fun build() = FakeTaskDataSource(
        tasks = tasks,
        task = mTask,
        errorRepose = errorRepose,
        emptyRepose = emptyRepose,
        nullResponse = nullResponse
    )
}

internal class FakeTaskDataSource(
    private val tasks: List<TaskDataModel>? = mutableListOf(),
    private val task: TaskDataModel?,
    private val errorRepose: Boolean = false,
    private val emptyRepose: Boolean = false,
    private val nullResponse: Boolean = false
): TasksDataSource {

    var createTaskCalled = false
        private set

    override suspend fun loadTasks(pageNo: Int, pageSize: Int): List<TaskDataModel>? {
        return when {
            errorRepose -> throw Exception("Error")
            emptyRepose -> emptyList()
            nullResponse -> null
            else -> tasks
        }
    }

    override suspend fun loadTask(taskId: Int): TaskDataModel? {
        return when {
            errorRepose -> throw Exception("Error")
            nullResponse -> null
            else -> task
        }
    }

    override suspend fun createTask(taskSubmissionModel: TaskSubmissionModel) {
        createTaskCalled = true
    }
}

