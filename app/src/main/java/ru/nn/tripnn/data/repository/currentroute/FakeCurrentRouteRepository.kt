package ru.nn.tripnn.data.repository.currentroute

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.nn.tripnn.data.datasource.currentroute.CurrentRouteDataSource
import ru.nn.tripnn.data.datasource.favourite.FavouritesDataSource
import ru.nn.tripnn.data.datasource.localroute.LocalRouteDataSource
import ru.nn.tripnn.data.datasource.stubdata.ui.CURRENT_ROUTE
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregator
import ru.nn.tripnn.data.repository.routebuilder.RouteBuilderService
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.domain.Route

class FakeCurrentRouteRepository(
    private val currentRouteDataSource: CurrentRouteDataSource,
    private val localRouteDataSource: LocalRouteDataSource,
    private val favouritesDataSource: FavouritesDataSource,
    private val placeDataAggregator: PlaceDataAggregator,
    private val routeBuilderService: RouteBuilderService
) : CurrentRouteRepository(
    currentRouteDataSource, localRouteDataSource, favouritesDataSource, placeDataAggregator, routeBuilderService, CoroutineScope(Dispatchers.IO)
) {
    override fun getCurrentRoute(createIfAbsent: Boolean): Flow<Result<CurrentRoute?>> {
        return flowOf(Result.success(CURRENT_ROUTE));
    }

    override suspend fun addCurrentRouteToFavourite(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun removeCurrentRouteFromFavourite(): Result<Unit> {
        return Result.success(Unit)
    }

    override fun currentRouteExists(): Flow<Result<Boolean>> {
        return flowOf(Result.success(true))
    }

    override suspend fun addPlaceToRoute(id: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun removePlaceFromRoute(index: Int): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun takeCurrentRoute(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun deleteCurrentRoute(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun clearCurrentRoute(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun takeTheRoute(route: Route): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun rateCurrentRoute(rating: Int): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun goToNextPlace(): Result<Unit> {
        return Result.success(Unit)
    }
}