package com.example.common_ui.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

private const val DEFAULT_ANIMATION_TIME = 600// IN MS

fun defaultNavigationEnterTransition(timeInMs: Int = DEFAULT_ANIMATION_TIME): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(timeInMs)
    ) + fadeIn(animationSpec = tween(timeInMs))
}

fun defaultNavigationExitTransition(timeInMs: Int = DEFAULT_ANIMATION_TIME): ExitTransition {
    return slideOutHorizontally (
        targetOffsetX = { fullWidth -> -fullWidth },
        animationSpec = tween(timeInMs)
    ) + fadeOut(animationSpec = tween(timeInMs))
}

fun defaultNavigationPopEnterTransition(timeInMs: Int = DEFAULT_ANIMATION_TIME): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(timeInMs)
    ) + fadeIn(animationSpec = tween(timeInMs))
}

fun defaultNavigationPopExitTransition(timeInMs: Int = DEFAULT_ANIMATION_TIME): ExitTransition {
    return slideOutHorizontally (
        targetOffsetX = { fullWidth -> -fullWidth },
        animationSpec = tween(timeInMs)
    ) + fadeOut(animationSpec = tween(timeInMs))
}