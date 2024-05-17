package ru.nn.tripnn.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

inline fun <T> request(request: () -> T) = try {
    Result.success(request())
} catch (e: Exception) {
    Result.failure(e)
}

fun <T> Flow<T>.toResultFlow(): Flow<Result<T>> {
    return catch { e -> Result.failure<T>(e) }.map { Result.success(it) }
}