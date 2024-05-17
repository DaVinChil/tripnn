package ru.nn.tripnn.data.datasource.history

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.nn.tripnn.data.database.place.history.PlacesHistoryDao
import ru.nn.tripnn.data.database.place.history.VisitedPlace
import ru.nn.tripnn.data.database.route.RouteReference
import ru.nn.tripnn.data.database.route.history.RoutesHistoryDao
import ru.nn.tripnn.data.database.route.history.TakenRoute
import ru.nn.tripnn.data.database.route.localroute.LocalRouteDao
import ru.nn.tripnn.data.datasource.AbstractDataSource
import ru.nn.tripnn.data.toResultFlow
import java.util.Date

class HistoryDataSourceImpl(
    private val placesHistoryDao: PlacesHistoryDao,
    private val routesHistoryDao: RoutesHistoryDao,
    private val localRouteDao: LocalRouteDao,
    ioDispatcher: CoroutineDispatcher
) : HistoryDataSource, AbstractDataSource(ioDispatcher) {
    override fun getAllVisitedPlaces(): Flow<Result<List<VisitedPlace>>> {
        return placesHistoryDao.getPlaces().toResultFlow()
    }

    override suspend fun deleteAllVisitedPlaces(): Result<Unit> {
        return dispatchedRequest { placesHistoryDao.clearPlacesHistory() }
    }

    override fun isPlaceVisitedById(id: String): Flow<Result<Boolean>> {
        return placesHistoryDao.findPlaceByPlaceId(id).map { it != null }.toResultFlow()
    }

    override suspend fun addPlaceToHistory(id: String): Result<Unit> {
        return dispatchedRequest { placesHistoryDao.savePlace(visitedPlace(id)) }
    }

    override suspend fun removePlaceFromHistory(id: String): Result<Unit> {
        return dispatchedRequest { placesHistoryDao.deletePlace(id) }
    }

    override fun getTakenRoutes(): Flow<Result<List<TakenRoute>>> {
        return routesHistoryDao.getRoutes().map { it ?: listOf() }.toResultFlow()
    }

    override suspend fun deleteAllTakenRoutes(): Result<Unit> {
        return dispatchedRequest { routesHistoryDao.clearRoutesHistory() }
    }

    override fun whenRouteWasTaken(route: RouteReference): Flow<Result<Date?>> {
        return when {
            route.remoteRouteId() != null -> whenRouteWasTakenByRemoteId(route.remoteRouteId()!!)
            route.localRouteId() != null -> whenRouteWasTakenByLocalId(route.localRouteId()!!)
            else -> flowOf(
                Result.failure(
                    IllegalStateException(
                        "Taken route should have localId or routeId. Have instead $route"
                    )
                )
            )
        }
    }

    private fun whenRouteWasTakenByRemoteId(id: Long): Flow<Result<Date?>> {
        return routesHistoryDao.findByRemoteId(id).map { it?.wasTakenAt }.toResultFlow()
    }

    private fun whenRouteWasTakenByLocalId(id: Long): Flow<Result<Date?>> {
        return routesHistoryDao.findByLocalId(id).map { it?.wasTakenAt }.toResultFlow()
    }

    override suspend fun addRouteToHistory(route: RouteReference): Result<Unit> {
        return dispatchedRequest {
            if (route.remoteRouteId() == null && route.localRouteId() == null) {
                throw IllegalStateException(
                    "Taken route should have localId or routeId. Have instead $route")
            }

            if (route.localRouteId() != null) {
                val referredRoute = localRouteDao.findById(route.localRouteId()!!)
                if (referredRoute == null) {
                    throw IllegalStateException(
                        "Taken route referencing to not existing local route with id ${route.localRouteId()}")
                }
            }

            routesHistoryDao.saveRoute(takenRoute(route))
        }
    }

    override suspend fun removeRouteFromHistory(route: RouteReference): Result<Unit> {
        return dispatchedRequest {
            when {
                route.remoteRouteId() != null -> routesHistoryDao.deleteByRemoteId(route.remoteRouteId()!!)
                route.localRouteId() != null -> routesHistoryDao.deleteByLocalId(route.localRouteId()!!)
                else -> throw IllegalStateException(
                    "Taken route should have localId or routeId. Have instead $route")
            }
        }
    }
}