package ru.nn.tripnn.ui.util

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retry

fun <T> Flow<T>.withRetry(): Flow<T> {
    return retry { e -> (e is CancellationException).also { if (it) delay(1000) } }
}