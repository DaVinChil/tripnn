package ru.nn.tripnn.data.datasource

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class AbstractDataSource(private val ioDispatcher: CoroutineDispatcher) {
    protected suspend fun <T> dispatchedRequest(
        request: suspend () -> T
    ): Result<T> = try {
        withContext(ioDispatcher) { Result.success(request()) }
    } catch (e: Exception) {
        Result.failure(e)
    }
}