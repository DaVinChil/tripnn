package ru.nn.tripnn.domain

import ru.nn.tripnn.ui.screen.main.search.Type

data class SearchFilters(
    val word: String? = null,
    val types: List<Type> = listOf(),
    val previousPlaceId: String? = null,
    val minRating: Float = 0f,
    val price: Int = -1,
    val maxDistance: Int? = null,
    val sortBy: Sort = Sort.RELEVANCE
)

enum class Sort {
    RELEVANCE, DISTANCE
}