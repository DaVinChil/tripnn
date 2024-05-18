package ru.nn.tripnn.domain.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

fun <T> Flow<Result<T>>.toResStateFlow(
    viewModelScope: CoroutineScope,
    initialValue: ResState<T> = ResState.loading(),
): StateFlow<ResState<T>> {
    return map { r -> r.toResState() }.catch { e -> ResState.Error<T>(e) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = initialValue
        )
}

fun <T> Result<T>.toResState(): ResState<T> = when {
    isSuccess -> ResState.Success(value = getOrThrow())
    else -> ResState.Error(error = exceptionOrNull())
}