package ru.nn.tripnn.data.local.favourite

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.data.local.favourite.place.FavouritePlace
import ru.nn.tripnn.data.local.favourite.place.FavouritePlacesDao
import ru.nn.tripnn.data.local.favourite.route.FavouriteRoute
import ru.nn.tripnn.data.local.favourite.route.FavouriteRoutesDao
import ru.nn.tripnn.data.remote.place.PlaceRepository
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route

class FavouritesRepositoryImpl(
    private val favouriteRoutesDao: FavouriteRoutesDao,
    private val favouritePlacesDao: FavouritePlacesDao,
    private val placeRepository: PlaceRepository,
    private val ioDispatcher: CoroutineDispatcher
) : FavouritesRepository {
    override suspend fun getFavouritePlaces(): RemoteResource<List<Place>> {
        return withContext(ioDispatcher) {
            val favouritePlaces = favouritePlacesDao.getPlaces()

            val resultPlaces = mutableListOf<Place>()
            favouritePlaces.forEach {
                when (val place = placeRepository.findById(it.placeId)) {
                    is RemoteResource.Success -> {
                        if (place.data != null) {
                            resultPlaces.add(place.data)
                        }
                    }

                    else -> {}
                }
            }

            RemoteResource.Success(resultPlaces)
        }
    }

    override suspend fun saveFavouritePlace(place: Place) {
        withContext(ioDispatcher) {
            favouritePlacesDao.savePlace(FavouritePlace(placeId = place.id))
        }
    }

    override suspend fun isFavouritePlace(place: Place): Boolean {
        return withContext(ioDispatcher) {
            val favouritePlace = favouritePlacesDao.findByPlaceId(place.id)
            favouritePlace != null
        }
    }

    override suspend fun deleteFavouritePlace(place: Place) {
        withContext(ioDispatcher) {
            favouritePlacesDao.deleteByRouteId(place.id)
        }
    }

    override suspend fun deleteAllFavouritePlaces() {
        withContext(ioDispatcher) {
            favouritePlacesDao.deleteAllFavouritePlaces()
        }
    }

    override suspend fun getFavouriteRoutes(): RemoteResource<List<Route>> {
        return withContext(ioDispatcher) {
            val routes = favouriteRoutesDao.getRoutes()

            val resultRoutes = routes.map { convertFavouriteRoute(it) }

            RemoteResource.Success(resultRoutes)
        }
    }

    private suspend fun convertFavouriteRoute(route: FavouriteRoute): Route {
        val places = mutableListOf<Place>()
        route.places.forEach {
            when (val place = placeRepository.findById(it.id)) {
                is RemoteResource.Success -> {
                    if (place.data != null) {
                        places.add(place.data)
                    }
                }

                is RemoteResource.Error -> {
                }
            }
        }
        return route.copy(places = places).convertToRoute()
    }

    override suspend fun saveFavouriteRoute(route: Route): Route {
        return withContext(ioDispatcher) {
            val localId = favouriteRoutesDao.saveRoute(FavouriteRoute.fromRoute(route))
            route.copy(localId = localId)
        }
    }

    override suspend fun isFavouriteRoute(route: Route): Boolean {
        return withContext(ioDispatcher) {
            val existingRoute = when {
                route.localId != 0 -> favouriteRoutesDao.findById(route.localId)
                route.id != null -> favouriteRoutesDao.findByRouteId(route.id)
                else -> null
            }

            existingRoute != null
        }
    }

    override suspend fun deleteFavouriteRoute(route: Route) {
        withContext(ioDispatcher) {
            if (route.localId != 0) {
                favouriteRoutesDao.deleteRouteById(route.localId)
            } else if (route.id != null) {
                favouriteRoutesDao.deleteRouteByRouteId(route.id)
            }
        }
    }

    override suspend fun deleteAllFavouriteRoutes() {
        withContext(ioDispatcher) {
            favouriteRoutesDao.deleteAllFavouriteRoutes()
        }
    }
}