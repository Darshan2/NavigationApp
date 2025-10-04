package com.example.jobs_ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.common_core.utils.Result
import com.example.jobs_ui.activity.ui.theme.NavigationAppTheme
import com.example.jobs_ui.events.TaskEvents
import com.example.jobs_ui.events.defaultTaskEvents
import com.example.jobs_ui.ui_models.TaskUiModel
import com.example.jobs_ui.viewmodel.TasksViewModel

@Composable
fun TaskDetailsScreen(modifier: Modifier = Modifier, jobId: Int) {
    val tasksViewModel = hiltViewModel<TasksViewModel>()

    LaunchedEffect(key1 = jobId) {
        tasksViewModel.loadTask(jobId)
    }
    val taskLoadState by tasksViewModel.taskLoadStatus.collectAsState()


    TaskDetailsLoadStatesLayout(
        modifier = modifier,
        taskLoadState = taskLoadState,
        taskEvents = tasksViewModel
    )
}


@Composable
fun TaskDetailsLoadStatesLayout(
    modifier: Modifier = Modifier,
    taskLoadState: Result<TaskUiModel>,
    taskEvents: TaskEvents
) {
    when(taskLoadState) {
        is Result.Idle -> {}
        is Result.Loading -> LoadingScreen(modifier = modifier)
        is Result.Error -> {
            ErrorScreen(modifier = modifier, message = taskLoadState.exception.message ?: "")
        }
        is Result.Success -> {
            TaskDetailsLayout(modifier = modifier, task = taskLoadState.data, taskEvents = taskEvents)
        }
    }
}

@Composable
fun TaskDetailsLayout(modifier: Modifier = Modifier, task: TaskUiModel, taskEvents: TaskEvents) {
    Column (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.wrapContentWidth(),
            style = MaterialTheme.typography.titleLarge,
            text = task.title
        )

        Spacer(Modifier.height(8.dp))

        Text(
            modifier = Modifier.wrapContentWidth(),
            style = MaterialTheme.typography.bodyLarge,
            text = task.description
        )
    }
}

@Preview
@Composable
private fun TaskDetailsScreenPreview() {
    NavigationAppTheme {
        TaskDetailsScreen(jobId = 1)
    }
}

@Preview
@Composable
private fun TaskDetailsLoadSuccessPreview() {
    NavigationAppTheme {
        TaskDetailsLoadStatesLayout(
            taskLoadState = Result.Success(TaskUiModel(id = 1, title = "Title,ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd", description = "Description")),
            taskEvents = defaultTaskEvents()
        )
    }
}

@Preview
@Composable
private fun TaskDetailsLoadLoadingPreview() {
    NavigationAppTheme {
        TaskDetailsLoadStatesLayout(
            taskLoadState = Result.Loading,
            taskEvents = defaultTaskEvents()
        )
    }
}

@Preview
@Composable
private fun TaskDetailsLoadFailurePreview() {
    NavigationAppTheme {
        TaskDetailsLoadStatesLayout(
            taskLoadState = Result.Error(Exception("Error")),
            taskEvents = defaultTaskEvents()
        )
    }
}
