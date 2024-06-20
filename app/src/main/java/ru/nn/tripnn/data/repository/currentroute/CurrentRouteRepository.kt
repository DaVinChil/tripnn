package ru.nn.tripnn.data.repository.currentroute

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.database.currentroute.CurrentRouteEntity
import ru.nn.tripnn.data.database.route.RouteReference
import ru.nn.tripnn.data.database.route.localroute.LocalRoute
import ru.nn.tripnn.data.datasource.currentroute.CurrentRouteDataSource
import ru.nn.tripnn.data.datasource.favourite.FavouritesDataSource
import ru.nn.tripnn.data.datasource.localroute.LocalRouteDataSource
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregator
import ru.nn.tripnn.data.repository.routebuilder.RouteBuilderService
import ru.nn.tripnn.data.toResultFlow
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.domain.WalkInfo
import java.time.LocalDate

open class CurrentRouteRepository(
    private val currentRouteDataSource: CurrentRouteDataSource,
    private val localRouteDataSource: LocalRouteDataSource,
    private val favouritesDataSource: FavouritesDataSource,
    private val placeDataAggregator: PlaceDataAggregator,
    private val routeBuilderService: RouteBuilderService,
    private val scope: CoroutineScope,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    open fun getCurrentRoute(createIfAbsent: Boolean = false): Flow<Result<CurrentRoute?>> {
        return currentRouteDataSource.getCurrentRoute().flatMapLatest { currentRoute ->
            val currentRouteEntity = if (createIfAbsent) {
                getOrCreateNewRoute(currentRoute)
            } else currentRoute.getOrThrow() ?: return@flatMapLatest flowOf(null)

            currentRouteFlowFrom(currentRouteEntity)
        }.toResultFlow()
    }

    private suspend fun currentRouteFlowFrom(currentRouteEntity: CurrentRouteEntity): Flow<CurrentRoute> {
        val favouriteFlow =
            if (currentRouteEntity.localRouteId != null || currentRouteEntity.remoteRouteId != null) {
                favouritesDataSource.isRouteFavourite(currentRouteEntity)
            } else {
                flowOf(Result.success(false))
            }

        return placeDataAggregator.placesFromIds(currentRouteEntity.places).map {
            val places = it.getOrThrow()
            val walkInfo = calcWalkInfo(places)
            Pair(places, walkInfo)
        }.combine(favouriteFlow) { placesAndWalkInfo, fav ->
            currentRouteEntity.toCurrentRoute(
                places = placesAndWalkInfo.first,
                walkInfo = placesAndWalkInfo.second,
                favourite = fav.getOrThrow()
            )
        }
    }

    open suspend fun addCurrentRouteToFavourite(): Result<Unit> {
        return try {
            getCurrentRoute().first().getOrThrow()?.let {
                favouritesDataSource.addRouteToFavourite(it)
            } ?: Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun removeCurrentRouteFromFavourite() = scope.async {
        try {
            getCurrentRoute().first().getOrThrow()?.let {
                favouritesDataSource.removeRouteFromFavourite(it)
            } ?: Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }.await()

    private suspend fun getOrCreateNewRoute(result: Result<CurrentRouteEntity?>): CurrentRouteEntity {
        return result.getOrThrow() ?: CurrentRouteEntity().also { createNewRoute() }
    }

    private suspend fun calcWalkInfo(places: List<Place>): List<WalkInfo> {
        val walkInfo = mutableListOf<WalkInfo>()

        for (i in 1 until places.size) {
            walkInfo.add(
                routeBuilderService.walkInfo(places[i - 1], places[i]).getOrThrow()
            )
        }

        return walkInfo
    }

    private suspend fun createNewRoute(): Result<Unit> =
        currentRouteDataSource.createNewCurrentRoute()

    open fun currentRouteExists(): Flow<Result<Boolean>> {
        return getCurrentRoute().map {
            val currentRoute = it.getOrThrow()
            currentRoute != null && !currentRoute.finished
        }.toResultFlow()
    }

    open suspend fun addPlaceToRoute(id: String): Result<Unit> =
        currentRouteDataSource.addPlaceToRoute(id)

    open suspend fun removePlaceFromRoute(index: Int): Result<Unit> =
        currentRouteDataSource.removePlaceFromRoute(index)

    open suspend fun takeCurrentRoute(): Result<Unit> = scope.async {
        try {
            val first = getCurrentRoute().first()
            val route = first.getOrThrow() ?: return@async Result.success(Unit)

            val id = localRouteDataSource.saveRoute(route.toLocalRoute()).getOrThrow()
            currentRouteDataSource.addRouteReference(object : RouteReference {
                override fun localRouteId() = id
                override fun remoteRouteId() = null
            })

            currentRouteDataSource.takeCurrentRoute()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }.await()

    open suspend fun deleteCurrentRoute(): Result<Unit> =
        currentRouteDataSource.deleteCurrentRoute()

    open suspend fun clearCurrentRoute(): Result<Unit> =
        currentRouteDataSource.clearCurrentRoute()

    open suspend fun takeTheRoute(route: Route): Result<Unit> =
        currentRouteDataSource.setCurrentRoute(route.toEntity())

    open suspend fun rateCurrentRoute(rating: Int): Result<Unit> {
        return currentRouteDataSource.getCurrentRoute().first().getOrNull()?.let {
            it.localRouteId?.let { routeId -> localRouteDataSource.rateRoute(rating, routeId) }
        } ?: Result.failure(NoSuchElementException())
    }

    open suspend fun goToNextPlace(): Result<Unit> {
        return currentRouteDataSource.goToNextPlace()
    }

    open suspend fun finishCurrentRoute(): Result<Unit> = currentRouteDataSource.finishCurrentRoute()

    private fun CurrentRouteEntity.toCurrentRoute(
        places: List<Place>,
        walkInfo: List<WalkInfo>,
        favourite: Boolean,
    ): CurrentRoute {
        return CurrentRoute(
            places = places,
            walkInfo = walkInfo,
            currentPlaceIndex = currentPlaceIndex,
            remoteRouteId = remoteRouteId,
            buildInProgress = buildInProgress,
            localRouteId = localRouteId,
            favourite = favourite,
            finished = finished
        )
    }

    private fun CurrentRoute.toLocalRoute(): LocalRoute {
        return LocalRoute(
            name = LocalDate.now().toString(),
            placeIds = places.map { it.id },
            imageUrl = places.first { it.photos.isNotEmpty() }.photos.first()
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