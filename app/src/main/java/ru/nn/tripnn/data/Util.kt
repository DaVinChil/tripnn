package ru.nn.tripnn.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import retrofit2.Response

inline fun <T> request(request: () -> T) = try {
    Result.success(request())
} catch (e: Exception) {
    Result.failure(e)
}

fun <T> Flow<T>.toResultFlow(): Flow<Result<T>> {
    return map {
        Result.success(it)
    }.catch { e ->
        emit(Result.failure(e))
    }
}

fun <T> Response<T>.getOrThrow(): T =
    when {
        isSuccessful -> body()!!
        else -> throw HttpException(this)
    }
