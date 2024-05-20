package ru.nn.tripnn.data.datasource.placeinfo

import ru.nn.tripnn.data.dto.PlaceDto
import ru.nn.tripnn.domain.SearchFilters

interface PlaceInfoDataSource {
    suspend fun findById(id: String): Result<PlaceDto>
    suspend fun findByIds(ids: List<String>): Result<List<PlaceDto>>
    suspend fun find(searchState: SearchFilters, page: Int?, pageSize: Int): Result<List<PlaceDto>>
}