package ru.nn.tripnn.domain.util

sealed class RemoteResource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): RemoteResource<T>(data)
    class Error<T>(message: String, data: T? = null): RemoteResource<T>(data, message)
}