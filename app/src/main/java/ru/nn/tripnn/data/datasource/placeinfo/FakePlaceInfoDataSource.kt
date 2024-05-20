package ru.nn.tripnn.data.datasource.placeinfo

import ru.nn.tripnn.data.datasource.stubdata.dto.DTO_PLACE_1
import ru.nn.tripnn.data.dto.PlaceDto
import ru.nn.tripnn.data.request
import ru.nn.tripnn.domain.SearchFilters

class FakePlaceInfoDataSource : PlaceInfoDataSource {
    private val places = listOf(DTO_PLACE_1)

    override suspend fun findById(id: String): Result<PlaceDto> {
        val place = places.find { it.id == id }

        if (place == null) return Result.failure(Exception())
        return Result.success(place)
    }

    override suspend fun findByIds(ids: List<String>): Result<List<PlaceDto>> = request {
        ids.map { findById(it).getOrThrow() }
    }

    override suspend fun find(searchState: SearchFilters, page: Int?, pageSize: Int): Result<List<PlaceDto>> {
        return Result.success(places)
    }
}