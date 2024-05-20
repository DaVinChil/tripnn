package ru.nn.tripnn.data.dto

data class SearchFiltersDto(
    val locale: String,
    val sort: String,
    val page: Int?,
    val word: String?,
    val lon: String?,
    val lat: String?,
    val minRating: Float,
    val priceRange: Int,
    val maxDistance: Int?,
    val isWorkingNow: Boolean,
    val types: List<Int>,
)