package ru.nn.tripnn.ui.common

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry

fun fullSlideOutVertical(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? {
    return {
        slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(300)
        )
    }
}

fun fullSlideInVertical(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? {
    return {
        slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(300)
        )
    }
}

fun fullSlideOutHorizontal(toRight: Boolean = true): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? {
    return {
        slideOutHorizontally(
            targetOffsetX = { if (toRight) it else -it },
            animationSpec = tween(300)
        )
    }
}

fun fullSlideInHorizontal(fromRight: Boolean = true): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? {
    return {
        slideInHorizontally(
            initialOffsetX = { if (fromRight) it else -it },
            animationSpec = tween(300)
        )
    }
}
    
