package com.example.jobs_core.data.repositoryImpls

import app.cash.turbine.test
import com.example.common_core.utils.Result
import com.example.jobs_core.data.model.TaskDataModel
import com.example.jobs_core.data.model.TaskSubmissionModel
import com.example.jobs_core.data.source.FakeTasksDataSourceBuilder
import com.example.test_utils.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TasksRepoImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var tasksRepoImpl: TasksRepoImpl

    // ---------- getTasks() ----------
    @Test
    fun `getTasks() return tasksList when dataSource return success`() = runTest {
        val fakeDataSource = FakeTasksDataSourceBuilder()
            .withTasks(listOf(
                    TaskDataModel(1, "1"),
                    TaskDataModel(2, "2")
                )
            ).build()
        tasksRepoImpl = TasksRepoImpl(fakeDataSource)

        val result = tasksRepoImpl.getTasks(1, 10)

        assertEquals(2, result.size)
        assertEquals("1", result.first().title)
    }

    @Test
    fun `getTasks() returns empty list when dataSource returns success and null`() = runTest {
        val fakeDataSource = FakeTasksDataSourceBuilder()
            .returnNullRepose()
            .build()
        tasksRepoImpl = TasksRepoImpl(fakeDataSource)

        val result = tasksRepoImpl.getTasks(1, 10)
        assertTrue(result.isEmpty())
    }

    @Test()
    fun `getTasks() throw error when dataSource returns error`() = runTest {
        val fakeDataSource = FakeTasksDataSourceBuilder()
            .returnErrorRepose()
            .build()
        tasksRepoImpl = TasksRepoImpl(fakeDataSource)

        assertFailsWith<Exception> {
            tasksRepoImpl.getTasks(1, 10)
        }
    }

    // ----------------------- getTask() ------------------------
    @Test
    fun `getTask() emit loading then success`() = runTest {
        val taskId = 40
        val fakeDataSource = FakeTasksDataSourceBuilder()
            .withTask(TaskDataModel(taskId, "Title: 40"))
            .build()
        tasksRepoImpl = TasksRepoImpl(fakeDataSource)

        tasksRepoImpl.getTask(taskId).test {
            assertTrue(awaitItem() is Result.Loading)

            val success = awaitItem()
            assertTrue(success is Result.Success)

            val data = (success as Result.Success).data
            assertEquals(taskId, data.id)

            awaitComplete()
        }
    }

    @Test
    fun `getTask() emit Loading then Error when data source returns error`() = runTest {
        val taskId = 40
        val fakeDataSource = FakeTasksDataSourceBuilder()
            .returnErrorRepose()
            .build()
        tasksRepoImpl = TasksRepoImpl(fakeDataSource)

        tasksRepoImpl.getTask(taskId).test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Error)
            awaitComplete()
        }
    }

    // -------------- createTask() ---------------
    @Test
    fun `createTask() emit Loading then Success and marks createTask called`() = runTest {
        val submission = TaskSubmissionModel("Title", "Desc", "Place")
        val fakeDataSource = FakeTasksDataSourceBuilder()
            .build()
        tasksRepoImpl = TasksRepoImpl(fakeDataSource)

        tasksRepoImpl.createTask(submission).test {
            assertTrue(awaitItem() is Result.Loading)
            assertTrue(awaitItem() is Result.Success)
            assertTrue(fakeDataSource.createTaskCalled)
            awaitComplete()
        }
    }
}