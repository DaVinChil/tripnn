package ru.nn.tripnn.data.local.currentroute

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.data.remote.place.PlaceRepository
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.domain.Place

class CurrentRouteRepository(
    private val currentRouteDao: CurrentRouteDao,
    private val placeRepository: PlaceRepository
) {
    suspend fun getCurrentRoute(): RemoteResource<CurrentRoute> {
        return withContext(Dispatchers.IO) {
            val currentRoute = currentRouteDao.getCurrentRoute()
                ?: return@withContext RemoteResource.Success(null)

            val listIds = currentRoute.places
            val places = mutableListOf<Place>()

            listIds.forEach {
                when (val place = placeRepository.findById(it.id)) {
                    is RemoteResource.Success -> {
                        if (place.data == null) {
                            return@withContext RemoteResource.Error(message = place.message ?: "")
                        }
                        places.add(place.data)
                    }

                    is RemoteResource.Error -> {
                        if (place.data == null) {
                            return@withContext RemoteResource.Error(message = place.message ?: "")
                        }
                    }
                }
            }

            RemoteResource.Success(currentRoute.copy(places = places))
        }
    }

    suspend fun saveCurrentRoute(currentRoute: CurrentRoute) =
        withContext(Dispatchers.IO) {
            currentRouteDao.saveCurrentRoute(currentRoute)
        }

    suspend fun deleteCurrentRoute() = withContext(Dispatchers.IO) {
        currentRouteDao.deleteCurrentRoute()
    }
}