package ru.nn.tripnn.ui.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.ui.screen.ResourceState

fun <T> resourceStateFromRequest(request: suspend () -> RemoteResource<T>): Flow<ResourceState<T>> =
    flow {
        emit(
            ResourceState(
                value = null,
                isError = false,
                error = null,
                isLoading = true
            )
        )

        when (val result = request()) {
            is RemoteResource.Success -> {
                emit(
                    ResourceState(
                        value = result.data,
                        isError = false,
                        error = null,
                        isLoading = false
                    )
                )
            }

            is RemoteResource.Error -> {
                emit(
                    ResourceState(
                        value = null,
                        isError = true,
                        error = result.message,
                        isLoading = false
                    )
                )
            }
        }
    }