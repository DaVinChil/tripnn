package ru.nn.tripnn.domain.model

import java.time.LocalTime

data class SearchFilters(
    val word: String,
    val types: List<Int>,
    val catalog: String,
    val prevPlaceId: String?,
    val userLocation: String?,
    val minPrice: Int?,
    val maxPrice: Int?,
    val workStartTime: LocalTime?,
    val workEndTime: LocalTime?,
    val minDistance: Int,
    val maxDistance: Int
)