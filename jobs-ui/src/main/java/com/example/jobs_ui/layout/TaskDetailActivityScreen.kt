package com.example.jobs_ui.layout

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.example.common_ui.layout.NavigationAppTopAppBar
import com.example.jobs_ui.R
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

//    NavigationAppTheme {
//        Scaffold(
//            topBar = {
//                NavigationAppTopAppBar(
//                    titleResId = R.string.task_detail_screen_title,
//                    showUpBtn = true,
//                    onUpBtnClick = { onBackPressedDispatcher?.onBackPressed() }
//                )
//            }
//        ) { paddingValues ->
//            TaskDetailsLoadStatesLayout(
//                modifier = Modifier.padding(paddingValues),
//                taskLoadState = taskLoadState,
//                taskEvents = tasksViewModel
//            )
//        }
//    }
}


@Composable
fun TaskDetailsLoadStatesLayout(
    modifier: Modifier = Modifier,
    taskLoadState: Result<TaskUiModel>,
    taskEvents: TaskEvents
) {
    when(taskLoadState) {
        is Result.Idle -> {}
        is Result.Loading -> LoadingScreen()
        is Result.Error -> {
            ErrorScreen(message = taskLoadState.exception.message ?: "")
        }
        is Result.Success -> {
            TaskDetailsLayout(modifier = modifier, task = taskLoadState.data, taskEvents = taskEvents)
        }
    }
}

@Composable
fun TaskDetailsLayout(modifier: Modifier = Modifier, task: TaskUiModel, taskEvents: TaskEvents) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 4.dp),
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
