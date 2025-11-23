package com.example.common.ui.utils.pagination

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

/**
 * Represents the different states of pagination.
 *
 * @param R The type of data held by the pagination states.
 */
sealed interface PaginationState<out R> {
    val data: R? get() = null
    /**
     * State indicating that the initial page of data is being loaded.
     */
    object LoadingInitial : PaginationState<Nothing>
    /**
     * State indicating that more data (subsequent pages) is being loaded.
     * @param data The currently accumulated data before fetching the next page.
     */
    data class LoadingMore<out T>(override val data: T) : PaginationState<T>

    /**
     * State indicating that a page of data has been successfully loaded.
     * @param data The data loaded, which might be the accumulated list or just the new page,
     *             depending on the paginator's configuration.
     */
    data class Success<out T>(override val data: T) : PaginationState<T>

    /**
     * State indicating that the list has been updated (e.g., items removed, added, or modified)
     * and the list size may have changed.
     * This event should be emitted primarily when the list's structure changes, not just an item's internal state.
     * @param data The current state of the data after the update.
     */
    data class Update<out T>(override val data: T) : PaginationState<T>


    data class Error(val accumulatedListSize: Int, val exception: Exception) : PaginationState<Nothing>

    /** State representing the end of pagination (no more data to load). */
    object End : PaginationState<Nothing>
}



/**
 * Represents different triggers that can initiate data loading or list update operations
 * in the [PageBasedPaginator].
 */
sealed class LoadTrigger {
    /**
     * Trigger to refresh the entire dataset, clearing existing data and starting from the first page.
     */
    object Refresh: LoadTrigger()

    /**
     * Trigger indicating that the list data has been updated locally (e.g., items removed)
     * and the paginator should emit an [PaginationState.Update].
     * @param listSize The new size of the list after the update.
     */
    data class Update(val listSize: Int): LoadTrigger()

    /**
     * Trigger to load a specific page of data.
     * @param pageNum The page number to be fetched.
     */
    data class NextPage(val pageNum: Int): LoadTrigger()
}

/**
 * Manages page-based pagination logic, handling data fetching, state updates, and optional transformations.
 *
 * This class fetches data in pages, accumulates it (optionally), transforms it, and exposes the
 * pagination state through a [Flow].
 *
 * @param DataType The type of data as it is fetched from the source (e.g., network DTO).
 * @param ExposedType The type of data after transformation, exposed to the consumer (e.g., UI model).
 * @property pageSize The number of items to fetch per page.
 * @property exposeAccumulatedList If `true`, [PaginationState.Success] will emit the entire accumulated
 *                                 list of items. If `false`, it will only emit the items from the
 *                                 newly fetched page. Defaults to `true`.
 * @property fetchPage A suspend function that takes the current page number and page size,
 *                     and returns a list of [DataType] items for that page.
 * @property transformer An optional suspend function to transform items from [DataType] to [ExposedType].
 *                       If null, a direct cast is attempted.
 * ### Usage Example
 *
 * ```kotlin
 * val paginator = PageBasedPaginator<TaskDataModel, TaskUiModel>(
 *     pageSize = 10,
 *     fetchPage = { pageNum, pageSize ->
 *         taskUseCase.loadTaskItems(pageNum, pageSize) // suspend fetch function
 *     },
 *     transformer = { data -> data.toUiModel() } // optional transformation
 * )
 *
 * // Collecting pagination state
 * viewModelScope.launch {
 *     paginator.pageState.collect { state ->
 *         when(state) {
 *             is PaginationState.LoadingInitial -> showLoading()
 *             is PaginationState.LoadingMore -> showLoadingMore(state.data)
 *             is PaginationState.Success -> displayItems(state.data)
 *             is PaginationState.Update -> updateList(state.data)
 *             is PaginationState.Error -> showError(state.exception)
 *             is PaginationState.End -> showEndOfList()
 *         }
 *     }
 * }
 *
 * // Loading next page
 * viewModelScope.launch {
 *     paginator.loadNextPage()
 * }
 *
 * // Refreshing the list
 * viewModelScope.launch {
 *     paginator.refreshPage()
 * }
 *
 * // Updating a specific item by predicate
 * paginator.updateItem(item) { it.id == item.id }
 *
 * // Removing multiple items by IDs
 * paginator.removeItemsByIds(listOf(1,2,3)) { it.id }
 */
class PageBasedPaginator<DataType, ExposedType>(
    private val pageSize: Int,
    private val exposeAccumulatedList: Boolean = true,
    private val fetchPage: suspend (pageNum: Int, pageSize: Int) -> List<DataType>,
    private val transformer: (suspend (DataType) -> ExposedType)? = null
) {
    private var currentPage = 1
    private var isLastPage = false

    private var accumulatedItems = mutableListOf<ExposedType>()

    private val _loadTrigger = MutableSharedFlow<LoadTrigger>(replay = 1)

    /**
     * A [Flow] that emits [PaginationState] updates, allowing consumers to react to
     * changes in the pagination process (loading, success, error, end of list).
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val pageState: Flow<PaginationState<List<ExposedType>>> = _loadTrigger
        .distinctUntilChanged { o, n -> o == n }
        .flatMapLatest { trigger ->
            when(trigger) {
                is LoadTrigger.Refresh -> {
                    reset()
                    loadNextPageData()
                }
                is LoadTrigger.NextPage -> {
                    loadNextPageData()
                }
                is LoadTrigger.Update -> {
                    emitUpdatedList()
                }
            }
        }

    /**
     * Triggers a full refresh of the data.
     * Clears any existing items and fetches data starting from page 1.
     * The new state will be emitted through [pageState].
     */
    suspend fun refreshPage() {
        _loadTrigger.emit(LoadTrigger.Refresh)
    }

    /**
     * Triggers the loading of the next available page of data.
     * The new state will be emitted through [pageState].
     */
    suspend fun loadNextPage() {
        _loadTrigger.emit(LoadTrigger.NextPage(currentPage))
    }

    /**
     * Creates a [Flow] that emits an [PaginationState.Update] with the current accumulated items.
     * This is typically used when the list has been modified locally (e.g., item removed)
     * and the change needs to be reflected to observers of [pageState].
     */
    private fun emitUpdatedList() = flow {
        emit(PaginationState.Update(accumulatedItems.toList()))
    }

    /**
     * Core logic for fetching, processing, and emitting the state for the next page of data.
     * This function is triggered by [LoadTrigger.Refresh] or [LoadTrigger.NextPage].
     *
     * It first checks if the last page has already been reached. If so, it emits [PaginationState.End].
     * Otherwise, it emits the appropriate loading state ([PaginationState.LoadingInitial] or
     * [PaginationState.LoadingMore]) via [getPageLoadingState].
     *
     * It then attempts to fetch new data using the provided [fetchPage] suspend function.
     * The fetched data is transformed using the optional [transformer] function or cast directly
     * if no transformer is provided.
     *
     * After processing, it updates the [isLastPage] flag if the fetched data indicates the end
     * of the dataset and increments the [currentPage] counter.
     *
     * Finally, it emits a [PaginationState.Success] with the new list of items. The content of this list
     * depends on the [exposeAccumulatedList] flag:
     *  - If `true`, it emits the entire accumulated list including the newly fetched items.
     *  - If `false`, it emits only the newly fetched items.
     *
     * If any exception occurs during fetching or transformation (especially [ClassCastException]),
     * it emits a [PaginationState.Error] and stops further processing for the current trigger.
     *
     * @return A [Flow] that emits the appropriate [PaginationState] for the loading operation.
     */
    private fun loadNextPageData(): Flow<PaginationState<List<ExposedType>>> = flow {
        if (isLastPage) {
            emit(PaginationState.End)
            return@flow
        }

        emit(getPageLoadingState())

        try {
            val fetchedDataItems = fetchPage(currentPage, pageSize)

            val newExposedItems: List<ExposedType> = if (transformer != null) {
                fetchedDataItems.map { transformer(it) }
            } else {
                @Suppress("UNCHECKED_CAST")
                fetchedDataItems as List<ExposedType>
            }

            if (newExposedItems.isEmpty() || newExposedItems.size < pageSize) isLastPage = true
            if (newExposedItems.isNotEmpty() || currentPage == 1) {
                currentPage++
            }

            val list = if(exposeAccumulatedList) {
                accumulatedItems.apply {
                    addAll(newExposedItems)
                }
            } else {
                newExposedItems
            }
            emit(PaginationState.Success(list.toList()))

        } catch (e: ClassCastException) {
            // If transformation or cast fails, emit error and finish this flow for this trigger
            emit(PaginationState.Error(accumulatedListSize = accumulatedItems.size,
                exception = Exception("Transformation/cast failed: ${e.message}", e)))
            return@flow
        } catch (e: Exception) {
            emit(PaginationState.Error(accumulatedListSize = accumulatedItems.size, exception = Exception(e)))
            return@flow
        }
    }

    /**
     * Determines the appropriate loading state ([PaginationState.LoadingInitial] or [PaginationState.LoadingMore])
     * based on whether any items have been accumulated yet.
     *
     * @return The current loading state.
     */
    private fun getPageLoadingState(): PaginationState<List<ExposedType>> {
        return if(accumulatedItems.isEmpty())
            PaginationState.LoadingInitial
        else
            PaginationState.LoadingMore(accumulatedItems)
    }

    /**
     * Returns a snapshot of the currently loaded and transformed items.
     *
     * @return A new [List] containing all items accumulated so far.
     */
    fun getLoadedItems(): List<ExposedType> {
        return accumulatedItems.toList()
    }

    fun getItemCount(): Int {
        return accumulatedItems.size
    }

    /**
     * Finds the index of the first occurrence of the specified item in the accumulated list.
     *
     * @param item The item to search for.
     * @return The index of the first occurrence of the item, or -1 if the item is not found.
     */
    fun getIndex(item: ExposedType): Int {
        return accumulatedItems.indexOf(item)
    }

    /**
     * Updates the item at the specified [index] in the accumulated list with the provided [item].
     * If the index is out of bounds, this operation has no effect.
     *
     * Note: This method modifies the local list directly but does *not* automatically
     * emit an [PaginationState.Update] via [pageState]. To notify observers of this change,
     * you might need to manually trigger an update or call [notifyListSizeChange] if the size changes.
     *
     * @param index The index of the item to replace.
     * @param item The new item to place at the specified index.
     */
    fun updateItem(index: Int, item: ExposedType) {
        if (index in accumulatedItems.indices) {
            accumulatedItems[index] = item
        }
    }

    /**
     * Updates an existing item in the accumulated list that matches the given predicate.
     *
     * If multiple items match the predicate, only the first one found will be updated.
     * If no item matches the predicate, or if the found index is otherwise invalid,
     * the list remains unchanged.
     *
     * Note: This method modifies the local list directly but does *not* automatically
     * emit an [PaginationState.Update] via [pageState]. To notify observers of this change,
     * you might need to manually trigger an update.
     *
     * @param item The new item that will replace the existing item if found.
     * @param comparator A lambda function that takes an [ExposedType] item from the list
     *                   and returns `true` if it's the item to be updated, `false` otherwise.
     * @return `true` if an item was found and updated, `false` otherwise.
     *
     * ### Usage Example
     * ```kotlin
     * val updated = paginator.updateItem(
     *     item = TaskUiModel(id = 42, title = "Updated Task")
     * ) { it.id == 42 }
     *
     * if (updated) {
     *     println("Task updated successfully")
     * } else {
     *     println("Task not found")
     * }
     * ```
     */
    fun updateItem(item: ExposedType, comparator: (ExposedType) -> Boolean): Boolean {
        val index = accumulatedItems.indexOfFirst(comparator)
        return if (index in accumulatedItems.indices) { // Check if index is valid and within bounds
            accumulatedItems[index] = item
            true
        } else {
            false
        }
    }


    /**
     * Removes the first occurrence of the specified [item] from the accumulated list.
     * If the item is removed, an [PaginationState.Update] is emitted via [pageState].
     *
     * @param item The item to be removed.
     */
    suspend fun removeItem(item: ExposedType) {
        val removed = accumulatedItems.remove(item)
        if(removed) {
           notifyListSizeChange()
        }
    }

    /**
     * Removes the first item from the accumulated list that satisfies the given [predicate].
     * If an item is removed, an [PaginationState.Update] is emitted via [pageState].
     *
     * @param predicate A function that returns `true` for the item that should be removed.

     * ### Usage Example
     * ```kotlin
     * // Remove the first task that is marked as completed
     * viewModelScope.launch {
     *     paginator.removeItem { task -> task.isCompleted }
     * }
     *
     * // After removal, pageState will emit an Update state reflecting the new list
     *
     */
    suspend fun removeItem(predicate: (ExposedType) -> Boolean) {
        val index = accumulatedItems.indexOfFirst(predicate)
        if (index in accumulatedItems.indices) {
            accumulatedItems.removeAt(index)
            notifyListSizeChange()
        }
    }

    /**
     * Removes all items from the accumulated list whose ID (extracted by [idSelector])
     * is present in the provided [ids] collection.
     * If items are removed, an [PaginationState.Update] is emitted via [pageState].
     *
     * @param ids A collection of IDs of items to be removed.
     * @param idSelector A function that extracts an ID (of type [Any]) from an [ExposedType] item.
     *
     * ### Usage Example
     * ```kotlin
     * // Suppose you want to remove tasks with IDs 1, 3, and 5
     * val idsToRemove = listOf(1, 3, 5)
     *
     * viewModelScope.launch {
     *     paginator.removeItemsByIds(idsToRemove) { task -> task.id }
     * }
     *
     * // After removal, pageState will emit an Update state reflecting the new list
     * ```
     */
    suspend fun removeItemsByIds(ids: Collection<Any>, idSelector: (ExposedType) -> Any) {
        val idsSet = ids.toSet()
        val removed = accumulatedItems.removeAll { item ->
            idSelector(item) in idsSet
        }
        if (removed) {
            notifyListSizeChange()
        }
    }

    /**
     * Emits a [LoadTrigger.Update] to the internal trigger flow, signaling that the
     * list size has changed. This typically results in an [PaginationState.Update]
     * being emitted from [pageState].
     */
    private suspend fun notifyListSizeChange() {
        _loadTrigger.emit(LoadTrigger.Update(accumulatedItems.size))
    }

    private fun reset() {
        currentPage = 1
        isLastPage = false
        accumulatedItems.clear()
    }
}


