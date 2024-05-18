package ru.nn.tripnn.domain.state

import androidx.compose.runtime.Immutable

@Immutable
sealed class ResState<T> {

    @Immutable
    class Success<T>(val value: T) : ResState<T>()

    @Immutable
    class Error<T>(val error: Throwable? = null) : ResState<T>()

    @Immutable
    data object Loading : ResState<Any>()

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun <T> loading(): ResState<T> {
            return Loading as ResState<T>
        }
    }

    fun getOrNull() = if (this is Success) value else null

    fun isError() = this is Error
    fun isSuccess() = this is Success
    fun isLoading() = this is Loading
}
