package ru.nn.tripnn.data.local.history

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.data.local.history.place.HistoryPlace
import ru.nn.tripnn.data.local.history.place.PlacesHistoryDao
import ru.nn.tripnn.data.local.history.route.HistoryRoute
import ru.nn.tripnn.data.local.history.route.RoutesHistoryDao
import ru.nn.tripnn.data.remote.place.PlaceRepository
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route

class HistoryRepositoryImpl(
    private val routesHistoryDao: RoutesHistoryDao,
    private val placesHistoryDao: PlacesHistoryDao,
    @Fake private val placeRepository: PlaceRepository,
    private val ioDispatcher: CoroutineDispatcher
) : HistoryRepository {
    override suspend fun getRoutes(): RemoteResource<List<Route>> {
        return withContext(ioDispatcher) {
            val routes = routesHistoryDao.getRoutes()

            val resultRoutes = mutableListOf<Route>()

            routes.forEach { route ->
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
                resultRoutes.add(route.copy(places = places).convertToRoute())
            }

            RemoteResource.Success(resultRoutes)
        }
    }


    override suspend fun getPlaces(): RemoteResource<List<Place>> {
        return withContext(ioDispatcher) {
            val historyPlaces = placesHistoryDao.getPlaces()

            val places = mutableListOf<Place>()
            historyPlaces.forEach {
                when(val response = placeRepository.findById(it.placeId)) {
                    is RemoteResource.Success -> {
                        if (response.data != null) {
                            places.add(response.data)
                        }
                    }

                    else -> {}
                }
            }

            return@withContext RemoteResource.Success(places)
        }
    }

    override suspend fun saveRoute(route: Route) {
        withContext(ioDispatcher) {
            routesHistoryDao.saveRoute(HistoryRoute.fromRoute(route))
        }
    }

    override suspend fun savePlace(place: Place) {
        withContext(ioDispatcher) {
            placesHistoryDao.savePlace(HistoryPlace(placeId = place.id))
        }
    }

    override suspend fun deleteRoute(route: Route) {
        withContext(ioDispatcher) {
            routesHistoryDao.deleteRoute(HistoryRoute.fromRoute(route))
        }
    }

    override suspend fun deletePlace(place: Place) {
        withContext(ioDispatcher) {
            placesHistoryDao.deletePlace(HistoryPlace(placeId = place.id))
        }
    }

    override suspend fun clearPlacesHistory() {
        withContext(ioDispatcher) {
            placesHistoryDao.clearPlacesHistory()
        }
    }

    override suspend fun clearRoutesHistory() {
        withContext(ioDispatcher) {
            routesHistoryDao.clearRoutesHistory()
        }
    }

    override suspend fun hasRoute(route: Route) =
        withContext(ioDispatcher) {
            if (route.id == null) return@withContext false
            return@withContext routesHistoryDao.hasByRouteId(route.id) >= 1
        }

    override suspend fun hasPlace(place: Place) =
        withContext(ioDispatcher) {
            return@withContext placesHistoryDao.hasByPlaceId(place.id) >= 1
        }
}