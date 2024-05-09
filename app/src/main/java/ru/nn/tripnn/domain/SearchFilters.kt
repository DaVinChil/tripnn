package ru.nn.tripnn.domain

import ru.nn.tripnn.ui.screen.main.search.Type

data class SearchFilters(
    val word: String? = null,
    val types: List<Type>? = null,
    val previousPlaceId: String? = null,
    val minRating: Float? = null,
    val price: Int = -1,
    val maxDistance: Int? = null,
    val sortBy: String = "relevance"
)