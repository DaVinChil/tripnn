package ru.nn.tripnn.domain.state

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResFlow<T>(
    private val initialState: ResState<T> = ResState.loading(),
    private val scope: CoroutineScope,
    private val supplier: () -> Flow<Result<T>>
) {
    private val flow = MutableStateFlow(requestState())

    val current get() = flow.value.value

    fun getOrNull(): T? {
        val local = current

        return if (local is ResState.Success) local.value else null
    }

    val state: StateFlow<ResState<T>>
        @Composable get() = flow.collectAsStateWithLifecycle().value

    private fun requestState(): StateFlow<ResState<T>> {
        return supplier().toResStateFlow(scope, initialState)
    }

    fun refresh() {
        scope.launch {
            flow.emit(requestState())
        }
    }

    fun isLoading(): Boolean = current is ResState.Loading
    fun isError(): Boolean = current is ResState.Error
    fun isSuccess(): Boolean = current is ResState.Success
}