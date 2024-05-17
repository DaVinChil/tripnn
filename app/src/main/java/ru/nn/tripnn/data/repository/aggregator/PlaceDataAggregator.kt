package ru.nn.tripnn.data.repository.aggregator

import kotlinx.coroutines.flow.Flow
import ru.nn.tripnn.data.dto.PlaceDto
import ru.nn.tripnn.domain.Place

interface PlaceDataAggregator {
    suspend fun placesFromIds(
        ids: List<String>
    ): Flow<Result<List<Place>>>

    suspend fun placesFromDtos(
        placeDtos: List<PlaceDto>
    ): Flow<Result<List<Place>>>

    fun placeFromId(
        id: String
    ): Flow<Result<Place>>

    fun placeFromDto(
        placeDto: PlaceDto
    ): Flow<Result<Place>>
}