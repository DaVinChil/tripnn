package ru.nn.tripnn.data.local.favourite

import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route

interface FavouritesRepository {
    suspend fun getFavouritePlaces(): RemoteResource<List<Place>>
    suspend fun saveFavouritePlace(place: Place)
    suspend fun isFavouritePlace(place: Place): Boolean
    suspend fun deleteFavouritePlace(place: Place)
    suspend fun deleteAllFavouritePlaces()

    suspend fun getFavouriteRoutes(): RemoteResource<List<Route>>
    suspend fun saveFavouriteRoute(route: Route): Route
    suspend fun isFavouriteRoute(route: Route): Boolean
    suspend fun deleteFavouriteRoute(route: Route)
    suspend fun deleteAllFavouriteRoutes()
}