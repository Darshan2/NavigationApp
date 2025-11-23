package com.example.jobs.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.core.utils.DEFAULT_TASK_ID
import com.example.common.core.utils.Result
import com.example.common.core.utils.toStateFlow
import com.example.common.core.utils.mapData
import com.example.common.ui.utils.pagination.PageBasedPaginator
import com.example.common.ui.utils.pagination.PaginationState
import com.example.jobs.core.data.source.remote.model.TaskDataModel
import com.example.jobs.core.domain.usecases.TaskUseCase
import com.example.jobs.ui.events.PaginationEvents
import com.example.jobs.ui.events.TaskEvents
import com.example.jobs.ui.uimodels.TaskUiModel
import com.example.jobs.ui.uimodels.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskUseCase: TaskUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel(), TaskEvents, PaginationEvents {

    companion object {
        const val TASKS_PAGE_SIZE = 10
        const val REMOVE_ANIMATION_TIME = 200L//in ms
        const val TASK_ID_KEY = "taskId"
    }

    private val itemsToDelete = mutableStateListOf<Int>()

    private val paginator: PageBasedPaginator<TaskDataModel, TaskUiModel> = PageBasedPaginator(
        pageSize = TASKS_PAGE_SIZE,
        fetchPage = { pageNum, pageSize ->
            taskUseCase.loadTaskItems(pageNum, pageSize)
        },
        transformer = {
            taskDataModel -> taskDataModel.toUiModel()
        }
    )

    val pageStatus = paginator.pageState.toStateFlow(
        viewModel = this,
        initialValue = PaginationState.LoadingInitial
    )

    init {
        observeItemsToDelete()
    }

    @OptIn(FlowPreview::class)
    fun observeItemsToDelete() {
        snapshotFlow { itemsToDelete.toList() }
            .distinctUntilChanged()
            .debounce(timeoutMillis = REMOVE_ANIMATION_TIME + 100)
            .onEach { deletionList ->
                if(deletionList.isNotEmpty()) {
                    paginator.removeItemsByIds(deletionList, idSelector = { item -> item.id })
                    itemsToDelete.removeAll(deletionList)
                }
            }
            .launchIn(viewModelScope)
    }


    override fun loadNextPage() {
        viewModelScope.launch {
            paginator.loadNextPage()
        }
    }

    override fun refreshPage() {
        itemsToDelete.clear()
        viewModelScope.launch {
            paginator.refreshPage()
        }
    }

    override fun updateTask(selected: Boolean, task: TaskUiModel) {
        task.selected.value = selected
        paginator.updateItem(task) {
            it.id == task.id
        }
    }

    override fun removeTask(task: TaskUiModel) {
        if(!itemsToDelete.contains(task.id)) {
            task.isMarkedForDelete.value = true
            itemsToDelete.add(task.id)
        }
    }


    val taskLoadStatus: StateFlow<Result<TaskUiModel>> =
        savedStateHandle.getStateFlow<Int>(TASK_ID_KEY, DEFAULT_TASK_ID)
            .flatMapLatest { taskId ->
                taskUseCase.loadTask(taskId).mapData { taskDataModel ->
                    taskDataModel.toUiModel()
                }
            }.toStateFlow(this, Result.Idle)

    //taskId is the most important key for the JobsDetailsScreen without it that screen can't work,
    //to handle the process death situation storing the id in SavedStateHandle
    fun loadTask(taskId: Int) {
        savedStateHandle[TASK_ID_KEY] = taskId
    }

    fun openTask(task: TaskUiModel) {
    }
}