package com.example.common_ui.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


/**
 * Collects a [Flow] in a lifecycle-aware manner, ensuring that collection
 * starts when the [LifecycleOwner] reaches the specified [state] and stops
 * when the [LifecycleOwner] moves out of that state (specifically, when it is [Lifecycle.State.STOPPED]
 * relative to the provided [state]).
 *
 * This extension function simplifies collecting flows from UI components like Activities or Fragments,
 * automatically managing the collection lifecycle to prevent resource leaks and unnecessary work
 * when the UI is not active.
 *
 * The collection is launched in the [LifecycleOwner.lifecycleScope]. The underlying mechanism
 * uses [Lifecycle.repeatOnLifecycle], which means the provided [collector] block will be
 * re-executed (and the flow re-collected) each time the lifecycle transitions into the target [state]
 * and cancelled when it moves out.
 *
 * @param T The type of data emitted by the Flow.
 * @param flow The [Flow] to collect.
 * @param state The [Lifecycle.State] at which the collection should start.
 *              Defaults to [Lifecycle.State.STARTED]. Collection will be active as long as the
 *              lifecycle is at least in this state.
 * @param collector A lambda function that will be invoked with each value emitted by the [flow].
 *
 * @see androidx.lifecycle.repeatOnLifecycle
 * @see kotlinx.coroutines.flow.Flow.collect
 */
fun <T> LifecycleOwner.collectFlow(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    collector: (T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(state) {
            flow.collect {
                collector(it)
            }
        }
    }
}