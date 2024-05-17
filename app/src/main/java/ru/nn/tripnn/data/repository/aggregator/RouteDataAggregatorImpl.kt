package ru.nn.tripnn.data.repository.aggregator

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.nn.tripnn.data.database.route.RouteReference
import ru.nn.tripnn.data.datasource.favourite.FavouritesDataSource
import ru.nn.tripnn.data.datasource.history.HistoryDataSource
import ru.nn.tripnn.data.datasource.localroute.LocalRouteDataSource
import ru.nn.tripnn.data.datasource.remoteroute.RemoteRouteDataSource
import ru.nn.tripnn.data.datasource.stubdata.ui.ROUTE_1
import ru.nn.tripnn.data.toResultFlow
import ru.nn.tripnn.domain.Route
import java.util.Date

class RouteDataAggregatorImpl(
    private val favouritesDataSource: FavouritesDataSource,
    private val historyDataSource: HistoryDataSource,
    private val remoteRouteDataSource: RemoteRouteDataSource,
    private val localRouteDataSource: LocalRouteDataSource,
    private val placeDataAggregator: PlaceDataAggregator
) : RouteDataAggregator {

    override suspend fun routes(
        routes: List<RouteReference>,
        favourite: Boolean?,
        wasTakenAt: List<Date>?
    ): Flow<Result<List<Route>>> {
        val resultRoutes = MutableList(routes.size) { ROUTE_1 }
        var resultFlow: Flow<List<Route>> = MutableStateFlow(resultRoutes)

        for (i in routes.indices) {
            val flow = when {
                routes[i].localRouteId() != null -> routeByLocalId(routes[i])
                routes[i].remoteRouteId() != null -> routeByRemoteId(routes[i])
                else -> throw IllegalStateException()
            }

            if (i == 0) {
                resultFlow = flow.map { route ->
                    resultRoutes.also { resultRoutes[i] = route.getOrThrow() }
                }
            } else {
                resultFlow = resultFlow.combine(flow) { _, route ->
                    resultRoutes.also { resultRoutes[i] = route.getOrThrow() }
                }.catch { e -> throw e }
            }
        }

        return resultFlow.toResultFlow()
    }

    private suspend fun routeByLocalId(
        id: RouteReference
    ): Flow<Result<Route>> = try {
        val localRoute = localRouteDataSource.findById(id.localRouteId()!!).getOrThrow()

        val route = localRoute.toRoute(favourite = false, wasTakenAt = null, places = listOf())

        val favouriteFlow = favouritesDataSource.isRouteFavourite(id)
        val wasTakenAtFlow = historyDataSource.whenRouteWasTaken(id)
        val placesFlow = placeDataAggregator.placesFromIds(localRoute.placeIds)

        favouriteFlow.map { f -> route.copy(favourite = f.getOrThrow()) }
            .combine(wasTakenAtFlow) { p, v -> p.copy(wasTakenAt = v.getOrThrow()) }
            .combine(placesFlow) { p, v -> p.copy(places = v.getOrThrow()) }
            .toResultFlow()
    } catch (e: Exception) {
        flowOf(Result.failure(e))
    }

    private suspend fun routeByRemoteId(
        id: RouteReference,
    ): Flow<Result<Route>> = try {
        val localRoute = remoteRouteDataSource.findById(id.remoteRouteId()!!).getOrThrow()

        val route = localRoute.toRoute(favourite = false, wasTakenAt = null, places = listOf())

        val favouriteFlow = favouritesDataSource.isRouteFavourite(id)
        val wasTakenAtFlow = historyDataSource.whenRouteWasTaken(id)
        val placesFlow = placeDataAggregator.placesFromDtos(localRoute.places)

        favouriteFlow.map { f -> route.copy(favourite = f.getOrThrow()) }
            .combine(wasTakenAtFlow) { p, v -> p.copy(wasTakenAt = v.getOrThrow()) }
            .combine(placesFlow) { p, ps -> p.copy(places = ps.getOrThrow()) }
            .toResultFlow()
    } catch (e: Exception) {
        flowOf(Result.failure(e))
    }
}