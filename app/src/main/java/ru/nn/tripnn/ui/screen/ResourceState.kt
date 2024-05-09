package ru.nn.tripnn.ui.screen

data class ResourceState<T>(
    val value: T? = null,
    val isError: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false
) {
    fun isSuccessAndNotNull() = !isLoading && !isError && value != null

    fun onNullFailCheck(check: T.() -> Boolean) = value?.check() ?: false
}