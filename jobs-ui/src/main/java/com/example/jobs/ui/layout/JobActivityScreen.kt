package com.example.jobs.ui.layout

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.common.ui.theme.NavigationAppTheme
import com.example.common.ui.utils.pagination.PaginationState
import com.example.jobs.ui.events.TaskEvents
import com.example.jobs.ui.events.defaultTaskEvents
import com.example.jobs.ui.uimodels.TaskUiModel
import com.example.jobs.ui.viewmodel.TasksViewModel

@Composable
fun TaskListScreen(modifier: Modifier = Modifier, openTask: (TaskUiModel) -> Unit) {
    val tasksViewModel = hiltViewModel<TasksViewModel>()
    val pageState by tasksViewModel.pageStatus.collectAsState()

    LaunchedEffect(tasksViewModel) {
        tasksViewModel.loadNextPage()
    }

    TaskListScreen(
        modifier = modifier,
        pageState = pageState,
        loadMore = tasksViewModel::loadNextPage,
        openTask =  openTask,
        taskEvents = tasksViewModel
    )
}

@Composable
fun TaskListScreen(
    modifier: Modifier = Modifier,
    pageState: PaginationState<List<TaskUiModel>>,
    loadMore: (() -> Unit),
    openTask: (TaskUiModel) -> Unit,
    taskEvents: TaskEvents
) {
    val scrollState = rememberLazyListState()

    NavigationAppTheme {
        when (pageState) {
            is PaginationState.End -> {}
            is PaginationState.Error -> ErrorScreen(message = pageState.exception.message ?: "")
            is PaginationState.LoadingInitial -> LoadingScreen()

            is PaginationState.Success,
            is PaginationState.LoadingMore,
            is PaginationState.Update -> {
                val showBottomLoader = pageState is PaginationState.LoadingMore
                TaskListScreen(
                    modifier = modifier,
                    taskList = pageState.data ?: emptyList(),
                    scrollState = scrollState,
                    showBottomLoader = showBottomLoader,
                    loadMore = loadMore,
                    openTask = openTask,
                    taskEvents = taskEvents
                )
            }
        }
    }
}

@Preview
@Composable
private fun TaskListScreenErrorState() {
    TaskListScreen(
        pageState = PaginationState.Error(0, Exception("Something went wrong")),
        loadMore = {},
        openTask = {},
        taskEvents = defaultTaskEvents()
    )
}

@Preview
@Composable
private fun TaskListScreenLoadingState() {
    TaskListScreen(
        pageState = PaginationState.LoadingInitial,
        loadMore = {},
        openTask = {},
        taskEvents = defaultTaskEvents()
    )
}

@Preview
@Composable
private fun TaskListScreenSuccessState() {
    TaskListScreen(
        pageState = PaginationState.Success(dummyTaskList()),
        loadMore = {},
        openTask = {},
        taskEvents = defaultTaskEvents()
    )
}

@Preview
@Composable
private fun TaskListScreenLoadMoreState() {
    TaskListScreen(
        pageState = PaginationState.LoadingMore(dummyTaskList()),
        loadMore = {},
        openTask = {},
        taskEvents = defaultTaskEvents()
    )
}