package ru.nn.tripnn.domain

import java.time.LocalTime

data class SearchFilters(
    val word: String? = null,
    val types: List<Int>? = null,
    val catalog: Int? = null,
    val previousPlaceId: String? = null,
    val userLocation: String? = null,
    val minRating: Float? = null,
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val workStartTime: LocalTime? = null,
    val workEndTime: LocalTime? = null,
    val maxDistance: Int? = null,
    val sortByDistance: Boolean = false,
    val sortByRating: Boolean = true
)