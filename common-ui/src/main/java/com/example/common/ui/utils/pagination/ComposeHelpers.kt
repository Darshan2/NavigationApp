package com.example.common.ui.utils.pagination

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow


/**
 * Observes the scroll state of a [LazyColumn] (or other Lazy layouts) and triggers a callback
 * when the user scrolls near the end of the list.
 *
 * This is useful for implementing **infinite scrolling / pagination**: as soon as the last
 * visible item index comes within [buffer] items of the end of the list, [loadMore] is invoked.
 *
 * The observation is performed inside a [LaunchedEffect] using [snapshotFlow], so it will
 * automatically collect and respond to scroll state changes while this composable is in composition.
 *
 * ### Example
 * ```
 * val listState = rememberLazyListState()
 *
 * EndOfListListener(
 *     scrollState = listState,
 *     buffer = 3
 * ) {
 *     // Load the next page of items here
 * }
 *
 * LazyColumn(state = listState) {
 *     items(myItems.size) { index ->
 *         Text("Item #$index")
 *     }
 * }
 * ```
 *
 * @param scrollState The [LazyListState] of the list to observe.
 * @param buffer The number of items before the end of the list at which to trigger [loadMore].
 *               For example, if set to 3, [loadMore] will be called when the user reaches within
 *               3 items from the end.
 * @param loadMore The callback invoked when the user has scrolled to the end (or near it).
 */
@Composable
fun EndOfListListener(
    scrollState: LazyListState,
    buffer: Int = 0,
    loadMore: () -> Unit
) {
    LaunchedEffect(key1 = scrollState) {
        snapshotFlow {
            scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.collect { lastVisibleItemIndex ->
            if (lastVisibleItemIndex != null) {
                val totalItems = scrollState.layoutInfo.totalItemsCount
                if (totalItems > 0) {

                    val itemsLeft = (totalItems - 1) - lastVisibleItemIndex
                    if (itemsLeft <= buffer) {
                        loadMore()
                    }
                }
            }
        }
    }
}