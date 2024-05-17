package ru.nn.tripnn.data.repository.history

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import ru.nn.tripnn.data.database.place.history.VisitedPlace
import ru.nn.tripnn.data.datasource.history.HistoryDataSource
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregator
import ru.nn.tripnn.data.repository.aggregator.RouteDataAggregator
import ru.nn.tripnn.data.toResultFlow
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route

class HistoryRepositoryImpl(
    private val historyDataSource: HistoryDataSource,
    private val routeDataAggregator: RouteDataAggregator,
    private val placeDataAggregator: PlaceDataAggregator
) : HistoryRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getTakenRoutes(): Flow<Result<List<Route>>> {
        return historyDataSource.getTakenRoutes()
            .flatMapLatest { routeDataAggregator.routes(it.getOrThrow()) }
            .onCompletion {
                emit(Result.success(listOf()))
                throw CancellationException()
            }
    }

    override suspend fun getTakenRoutesByWord(word: String): Flow<Result<List<Route>>> {
        val historyFlow = getTakenRoutes()

        return if (word.isNotEmpty()) {
            historyFlow
                .map { it.getOrThrow().filter { route -> route.title.contains(word) } }
                .toResultFlow()
        } else {
            historyFlow
        }
    }

    override suspend fun removeRouteFromHistory(route: Route): Result<Unit> {
        return historyDataSource.removeRouteFromHistory(route)
    }

    override suspend fun addRouteToHistory(route: Route): Result<Unit> {
        return historyDataSource.addRouteToHistory(route)
    }

    override suspend fun clearRoutesHistory(): Result<Unit> {
        return historyDataSource.deleteAllTakenRoutes()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getVisitedPlaces(): Flow<Result<List<Place>>> {
        val visitedPlaces = historyDataSource.getAllVisitedPlaces()

        return visitedPlaces.flatMapLatest {
            placeDataAggregator.placesFromIds(
                it.getOrThrow().map(VisitedPlace::placeId)
            )
        }
    }

    override suspend fun getVisitedPlacesByWord(word: String): Flow<Result<List<Place>>> {
        val visitedFlow = getVisitedPlaces()

        return if (word.isNotEmpty()) {
            visitedFlow
                .map { it.getOrThrow().filter { place -> place.name.contains(word) } }
                .toResultFlow()
        } else {
            visitedFlow
        }
    }

    override suspend fun addPlaceToHistory(place: Place): Result<Unit> {
        return historyDataSource.addPlaceToHistory(place.id)
    }

    override suspend fun removePlaceFromHistory(place: Place): Result<Unit> {
        return historyDataSource.removePlaceFromHistory(place.id)
    }

    override suspend fun clearPlacesHistory(): Result<Unit> {
        return historyDataSource.deleteAllVisitedPlaces()
    }
}