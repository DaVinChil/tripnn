package ru.nn.tripnn.data.repository.aggregator

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.nn.tripnn.data.datasource.favourite.FavouritesDataSource
import ru.nn.tripnn.data.datasource.history.HistoryDataSource
import ru.nn.tripnn.data.datasource.placeinfo.PlaceInfoDataSource
import ru.nn.tripnn.data.datasource.stubdata.ui.PLACE_1
import ru.nn.tripnn.data.dto.PlaceDto
import ru.nn.tripnn.data.toResultFlow
import ru.nn.tripnn.domain.Place

class PlaceDataAggregatorImpl(
    private val favouritesDataSource: FavouritesDataSource,
    private val historyDataSource: HistoryDataSource,
    private val placeInfoDataSource: PlaceInfoDataSource
) : PlaceDataAggregator {

    override suspend fun placesFromIds(
        ids: List<String>
    ): Flow<Result<List<Place>>> {
        val placesInfo = placeInfoDataSource.findByIds(ids)

        if (placesInfo.isFailure) {
            return flowOf(Result.failure(placesInfo.exceptionOrNull() ?: Exception()))
        }

        val places = placesInfo.getOrThrow()

        return placesFromDtos(places)
    }

    override suspend fun placesFromDtos(
        placeDtos: List<PlaceDto>
    ): Flow<Result<List<Place>>> {
        val resultPlaces = MutableList(placeDtos.size) { PLACE_1 }
        var resultFlow: Flow<List<Place>> = MutableStateFlow(resultPlaces)

        for (i in placeDtos.indices) {
            if (i == 0) {
                resultFlow = placeFromDto(placeDtos[i]).map { place ->
                    resultPlaces[i] = place.getOrThrow()
                    resultPlaces
                }
            } else {
                resultFlow = resultFlow.combine(placeFromDto(placeDtos[i])) { _, place ->
                    resultPlaces[i] = place.getOrThrow()
                    resultPlaces
                }.catch { e -> throw e }
            }
        }

        return resultFlow.toResultFlow()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun placeFromId(
        id: String
    ): Flow<Result<Place>> = flow {
        emit(placeInfoDataSource.findById(id))
    }
        .flatMapLatest { placeFromDto(it.getOrThrow()) }
        .catch { e -> Result.failure<Place>(e) }

    override fun placeFromDto(
        placeDto: PlaceDto
    ): Flow<Result<Place>> {
        val place = placeDto.toPlace(favourite = false, visited = false)

        val favouriteFlow = favouritesDataSource.isPlaceFavourite(place.id)
        val visitedFlow = historyDataSource.isPlaceVisitedById(place.id)

        return favouriteFlow
            .map { f -> place.copy(favourite = f.getOrThrow()) }
            .combine(visitedFlow) { p, v -> p.copy(visited = v.getOrThrow()) }
            .toResultFlow()
    }
}