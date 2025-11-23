package com.example.jobs.ui.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.common.ui.utils.pagination.EndOfListListener
import com.example.jobs.ui.events.TaskEvents
import com.example.jobs.ui.events.defaultTaskEvents
import com.example.jobs.ui.uimodels.TaskUiModel


@Composable
fun TaskListScreen(
    modifier: Modifier = Modifier,
    taskList: List<TaskUiModel>,
    scrollState: LazyListState,
    showBottomLoader: Boolean = false,
    loadMore: (() -> Unit)? = null,
    openTask: (TaskUiModel) -> Unit,
    taskEvents: TaskEvents
) {

    if(loadMore != null) {
        EndOfListListener(
            scrollState = scrollState,
            buffer = 2,
            loadMore = loadMore
        )
    }

    TaskList(modifier,
        scrollState = scrollState,
        showLoader = showBottomLoader,
        taskList = taskList,
        openTask = openTask,
        taskEvents = taskEvents
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskList(
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
    showLoader: Boolean = false,
    taskList: List<TaskUiModel>,
    openTask: (TaskUiModel) -> Unit,
    taskEvents: TaskEvents
) {
    LazyColumn(
        modifier.fillMaxSize(),
        state = scrollState,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = taskList, key = {task -> task.id}) { item ->
            TaskListItem(
                task = item,
                openTask = openTask,
                taskEvents = taskEvents
            )
        }
        if(showLoader) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun TaskListItem(
    modifier: Modifier = Modifier,
    task: TaskUiModel,
    openTask: (TaskUiModel) -> Unit,
    taskEvents: TaskEvents
) {

    AnimatedVisibility(
        modifier = modifier,
        visible = !task.isMarkedForDelete.value,
        exit = shrinkVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    ){
        Card(
            modifier = modifier.clickable(
                onClick = { openTask(task) }
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.width(20.dp),
                    checked = task.selected.value,
                    onCheckedChange = {
                        taskEvents.updateTask(it, task)
                    }
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall,
                    text = task.title
                )

                Icon(
                    modifier = Modifier
                        .defaultMinSize(24.dp)
                        .clickable { taskEvents.removeTask(task) },
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Icon"
                )

            }
        }

    }
}


@Preview
@Composable
private fun TaskListPreview() {
    TaskList(taskList = dummyTaskList(), taskEvents = defaultTaskEvents(), openTask = {})
}

@Preview
@Composable
private fun TaskListScreenWithProgressPreview() {
    TaskListScreen(
        taskList = dummyTaskList(),
        showBottomLoader = true,
        scrollState = rememberLazyListState(),
        loadMore = {},
        openTask = {},
        taskEvents = defaultTaskEvents()
    )
}

internal fun dummyTaskList(): List<TaskUiModel> {
    val items = mutableListOf<TaskUiModel>()
    for(i in 0..30) {
        items.add(TaskUiModel(id = i, title = "Item: $i", description = "Description: $i"))
    }
    return items
}