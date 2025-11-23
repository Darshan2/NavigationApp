package com.example.jobs.core.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.jobs.core.data.mappers.toDomainModel
import com.example.jobs.core.data.source.local.dao.TimerDao
import com.example.jobs.core.domain.model.Timer
import javax.inject.Inject

/**
 * A [PagingSource] implementation that loads [Timer] items from the local database
 * in descending order of their `updatedOnInMillis` field (most recently updated first).
 *
 * This is a **cursor-based pagination** strategy using the `updatedOnInMillis`
 * timestamp as the pagination key. Each page request fetches a fixed number of
 * timers whose `updatedOnInMillis` value is **less than** the last loaded item's timestamp.
 *
 * Flow overview:
 * 1. The first load (initial page) uses [Long.MAX_VALUE] as the cursor, effectively loading the
 *    most recently updated timers.
 * 2. For subsequent loads, [params.key] provides the `updatedOnInMillis` of the last item
 *    from the previous page, ensuring continuity.
 * 3. Each batch of timers is enriched by fetching their associated [TimerInterval] entries
 *    and mapping them into domain models.
 *
 * Paging keys:
 * - [prevKey] is always `null` since loading occurs only in the forward (older) direction.
 * - [nextKey] is the `updatedOnInMillis` of the last loaded timer, used as the cursor
 *   for the next page request.
 *
 * Error handling:
 * - Any exception thrown during loading or mapping is caught and converted into
 *   [LoadResult.Error], allowing the Paging library to handle retries gracefully.
 *
 * Refresh behavior:
 * - [getRefreshKey] returns the `updatedOnInMillis` of the item closest to the user’s
 *   current scroll position. This keeps the user approximately in the same area
 *   of the list after a refresh.
 *
 * @property timerDao Data access object that provides timer and timer interval queries.
 */
class TimerPagingSource @Inject constructor(
    private val timerDao: TimerDao
): PagingSource<Long, Timer>() {

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Timer> {
        return try {
            val pageSize = params.loadSize
            val lastUpdatedOn = params.key ?: Long.MAX_VALUE

            val timerEntities = timerDao.getPagedTimers(pageSize, lastUpdatedOn)
            val timerIds = timerEntities.map { it.id }
            val timerIdToIntervalsMap =
                timerDao.getTimerIntervalsForTimers(timerIds).groupBy { it.timerId }

            val timerWithTimeIntervals = timerEntities.map { timerEntity ->
                Timer(
                    id = timerEntity.id,
                    name = timerEntity.name,
                    updatedOnInMillis = timerEntity.updateOnInMillis,
                    timerIntervals = timerIdToIntervalsMap[timerEntity.id]?.map { it ->
                        it.toDomainModel()
                    } ?: mutableListOf()
                )
            }

            LoadResult.Page(
                data = timerWithTimeIntervals,
                prevKey = null,
                nextKey = timerEntities.lastOrNull()?.updateOnInMillis
            )

        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    /**
     * Determines the key to use when the list is refreshed.
     *
     * This method helps the Paging library decide what data to load after a refresh event.
     * A refresh can happen in two main scenarios:
     *
     * 1. **User-triggered refresh** — such as a swipe-to-refresh gesture or a re-collection
     *    of the Flow backing the PagingData. In this case, the key ensures that
     *    the new data load starts near the user’s currently visible item, maintaining
     *    visual continuity.
     *
     * 2. **Data source invalidation** — when the underlying database table is updated
     *    (for example, new timers are inserted, updated, or deleted). The Paging library
     *    automatically invalidates the [PagingSource] to ensure that the displayed data
     *    reflects the latest database state. In this case, the refresh key anchors
     *    the new query around the most recently accessed item, so that the user’s
     *    scroll position remains consistent even after new data arrives.
     *
     * Returning `null` here tells Paging to perform a fresh load from the default key
     * (i.e., start of dataset) if no anchor position is available.
     *
     * @param state Current [PagingState] containing information about loaded pages
     *              and the user’s last accessed item.
     * @return The [Timer.updatedOnInMillis] of the closest visible item, or `null`
     *         if the current position is unavailable.
     */
    override fun getRefreshKey(state: PagingState<Long, Timer>): Long? {
        // The most recently accessed index in the list
        val anchorPosition = state.anchorPosition ?: return null
        return state.closestItemToPosition(anchorPosition)?.updatedOnInMillis
    }
}
