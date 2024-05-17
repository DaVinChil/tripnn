package ru.nn.tripnn.ui.screen

import androidx.compose.runtime.Immutable


@Immutable
open class ResourceState<T>(
    val state: T? = null,
    val isError: Boolean = false,
    val error: Throwable? = null,
    val isLoading: Boolean = false
) {

    companion object {
        private val LOADING_STATE = LoadingState<Any>()

        @Suppress("UNCHECKED_CAST")
        fun <T> loading(): ResourceState<T> {
            return LOADING_STATE as ResourceState<T>
        }
    }

    fun isSuccessAndNotNull() = !isLoading && !isError && state != null

    fun toError(exception: Throwable? = null): ResourceState<T> {
        return ResourceState(state = null, isError = true, error = exception, isLoading = false)
    }

    fun toSuccess(state: T? = this.state): ResourceState<T> {
        return ResourceState(state = state, isError = false, error = null, isLoading = false)
    }

    fun copy(
        state: T? = this.state,
        isError: Boolean = this.isError,
        error: Throwable? = this.error,
        isLoading: Boolean = this.isLoading
    ): ResourceState<T> {
        return ResourceState(state, isError, error, isLoading)
    }
}

internal class LoadingState<E> : ResourceState<E>(
    state = null,
    isError = false,
    isLoading = true,
    error = null
) {

}