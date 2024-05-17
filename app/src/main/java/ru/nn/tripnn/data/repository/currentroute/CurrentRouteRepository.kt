package ru.nn.tripnn.data.repository.currentroute

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.nn.tripnn.data.database.currentroute.CurrentRouteEntity
import ru.nn.tripnn.data.datasource.currentroute.CurrentRouteDataSource
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregator
import ru.nn.tripnn.data.repository.routebuilder.RouteBuilderService
import ru.nn.tripnn.data.toResultFlow
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route

class CurrentRouteRepository(
    private val currentRouteDataSource: CurrentRouteDataSource,
    private val placeDataAggregator: PlaceDataAggregator,
    private val routeBuilderService: RouteBuilderService
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getCurrentRoute(): Flow<Result<CurrentRoute?>> {
        return currentRouteDataSource.getCurrentRoute().flatMapLatest { currentRoute ->
            val currentRouteEntity = currentRoute.getOrThrow()
                ?: return@flatMapLatest flowOf(Result.success(null))

            placeDataAggregator.placesFromIds(currentRouteEntity.places).map {
                val places = it.getOrThrow()
                val timeToWalk = mutableListOf<Int>()
                for (i in 1 until places.size) {
                    timeToWalk.add(
                        routeBuilderService.timeToWalk(places[i - 1], places[i]).getOrThrow()
                    )
                }
                currentRouteEntity.toCurrentRoute(places = places, timeToWalk = timeToWalk)
            }.toResultFlow()
        }.catch { e -> Result.failure<CurrentRoute?>(e) }
    }

    suspend fun addPlaceToRoute(id: String): Result<Unit> =
        currentRouteDataSource.addPlaceToRoute(id)

    suspend fun removePlaceFromRoute(index: Int): Result<Unit> =
        currentRouteDataSource.removePlaceFromRoute(index)

    suspend fun takeCurrentRoute(): Result<Unit> =
        currentRouteDataSource.takeCurrentRoute()

    suspend fun deleteCurrentRoute(): Result<Unit> =
        currentRouteDataSource.deleteCurrentRoute()

    suspend fun clearCurrentRoute(): Result<Unit> =
        currentRouteDataSource.clearCurrentRoute()

    suspend fun takeTheRoute(route: Route): Result<Unit> =
        currentRouteDataSource.setCurrentRoute(route.toEntity())

    private fun CurrentRouteEntity.toCurrentRoute(
        places: List<Place>,
        timeToWalk: List<Int>
    ): CurrentRoute {
        return CurrentRoute(
            places = places,
            timeToWalk = timeToWalk,
            currentPlaceIndex = currentPlaceIndex,
            remoteRouteId = remoteRouteId,
            buildInProgress = buildInProgress
        )
    }

    private fun Route.toEntity(): CurrentRouteEntity {
        return CurrentRouteEntity(
            places = places.map { it.id },
            currentPlaceIndex = 0,
            buildInProgress = false,
            remoteRouteId = remoteId
        )
    }
}