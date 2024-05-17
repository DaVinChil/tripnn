package ru.nn.tripnn.data.datasource.favourite

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import ru.nn.tripnn.data.database.place.favourite.FavouritePlace
import ru.nn.tripnn.data.database.place.favourite.FavouritePlacesDao
import ru.nn.tripnn.data.database.route.RouteReference
import ru.nn.tripnn.data.database.route.favourite.FavouriteRoute
import ru.nn.tripnn.data.database.route.favourite.FavouriteRoutesDao
import ru.nn.tripnn.data.database.route.localroute.LocalRouteDao
import ru.nn.tripnn.data.datasource.AbstractDataSource
import ru.nn.tripnn.data.toResultFlow

class FavouritesDataSourceImpl(
    private val favouritePlacesDao: FavouritePlacesDao,
    private val favouriteRoutesDao: FavouriteRoutesDao,
    private val localRouteDao: LocalRouteDao,
    ioDispatcher: CoroutineDispatcher
) : FavouritesDataSource, AbstractDataSource(ioDispatcher) {
    override fun getAllFavouritePlaces(): Flow<Result<List<FavouritePlace>>> {
        return favouritePlacesDao.getPlaces()
            .onCompletion { emit(listOf()); throw CancellationException() }.toResultFlow()
    }

    override suspend fun deleteAllFavouritePlaces(): Result<Unit> {
        return dispatchedRequest(favouritePlacesDao::deleteAllFavouritePlaces)
    }

    override suspend fun removePlaceFromFavourite(id: String): Result<Unit> {
        return dispatchedRequest { favouritePlacesDao.deleteByPlaceId(id) }
    }

    override suspend fun addPlaceToFavourite(id: String): Result<Unit> {
        return dispatchedRequest { favouritePlacesDao.savePlace(favouritePlace(id)) }
    }

    override fun isPlaceFavourite(id: String): Flow<Result<Boolean>> {
        return favouritePlacesDao.findByPlaceId(id).map { it != null }.toResultFlow()
    }

    override fun getAllFavouriteRoutes(): Flow<Result<List<FavouriteRoute>>> {
        return favouriteRoutesDao.getRoutes()
            .onCompletion { emit(listOf()); throw CancellationException() }.toResultFlow()
    }

    override suspend fun deleteAllFavouriteRoutes(): Result<Unit> {
        return dispatchedRequest(favouriteRoutesDao::deleteAllFavouriteRoutes)
    }

    override suspend fun removeRouteFromFavourite(route: RouteReference): Result<Unit> {
        return dispatchedRequest {
            when {
                route.localRouteId() != null -> favouriteRoutesDao.deleteByLocalId(route.localRouteId()!!)
                route.remoteRouteId() != null -> favouriteRoutesDao.deleteByRemoteId(route.remoteRouteId()!!)
                else -> throw IllegalStateException("Favourite route should have localId or routeId. Have instead $route")
            }
        }
    }

    override suspend fun addRouteToFavourite(route: RouteReference): Result<Unit> {
        return dispatchedRequest {
            when {
                route.localRouteId() != null && localRouteDao.findById(route.localRouteId()!!) == null ->
                    throw IllegalStateException(
                        "Favourite route referencing to not existing local route with id ${route.localRouteId()}"
                    )

                route.remoteRouteId() == null && route.localRouteId() == null ->
                    throw IllegalStateException(
                        "Favourite route should have localId or routeId. Have instead $route"
                    )

                else -> favouriteRoutesDao.saveRoute(favouriteRoute(route))
            }
        }
    }

    override fun isRouteFavourite(route: RouteReference): Flow<Result<Boolean>> {
        return when {
            route.localRouteId() != null -> isRouteFavouriteByLocalId(route.localRouteId()!!)
            route.remoteRouteId() != null -> isRouteFavouriteByRemoteId(route.remoteRouteId()!!)
            else -> throw IllegalStateException(
                "Favourite route should have localId or routeId. Have instead $route"
            )
        }
    }

    private fun isRouteFavouriteByRemoteId(id: Long): Flow<Result<Boolean>> {
        return favouriteRoutesDao.findByRemoteId(id).map { it != null }.toResultFlow()
    }

    private fun isRouteFavouriteByLocalId(id: Long): Flow<Result<Boolean>> {
        return favouriteRoutesDao.findByLocalId(id).map { it != null }
            .onCompletion { emit(false); throw CancellationException() }.toResultFlow()
    }

}