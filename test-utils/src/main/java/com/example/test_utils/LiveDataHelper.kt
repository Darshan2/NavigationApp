package com.example.test_utils

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Gets the value of a [LiveData] safely for unit tests.
 *
 * This extension function observes the [LiveData] until a value is set, and then returns that value.
 * It is designed to handle the asynchronous nature of [LiveData] in a testing environment by blocking
 * the current thread for a specified amount of time until the [LiveData] emits a value.
 *
 * If the [LiveData] already has a value, it will be returned immediately. Otherwise, it will wait
 * for the specified duration. If no value is set within the timeout period, it throws a
 * [TimeoutException].
 *
 * This is a common pattern for testing `LiveData` objects, as described in the official Android
 * Developers blog post.
 *
 * @param time The maximum time to wait for a value to be set. Defaults to 2.
 * @param timeUnit The unit of time for the `time` parameter. Defaults to [TimeUnit.SECONDS].
 * @param afterObserve A lambda function to be executed after the observer is registered. This is
 * useful for triggering actions that cause the [LiveData] to update, such as calling a
 * ViewModel function.
 * @return The value of the [LiveData].
 * @throws TimeoutException if the [LiveData] does not receive a value within the specified time.
 * @see <a href="https://medium.com/androiddevelopers/unit-testing-livedata-and-other-common-observability-problems-bb477262eb04">Unit Testing LiveData</a>
 */
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    try {
        afterObserve.invoke()

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

    } finally {
        this.removeObserver(observer)
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}