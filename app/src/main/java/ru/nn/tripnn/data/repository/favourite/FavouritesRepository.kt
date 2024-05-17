package ru.nn.tripnn.data.repository.favourite

import kotlinx.coroutines.flow.Flow
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route

interface FavouritesRepository {
    suspend fun getFavouritePlaces(): Flow<Result<List<Place>>>
    suspend fun getFavouritePlacesByWord(word: String): Flow<Result<List<Place>>>
    suspend fun addPlaceToFavourite(place: Place): Result<Unit>
    suspend fun isFavouritePlace(place: Place): Flow<Result<Boolean>>
    suspend fun removePlaceFromFavourite(place: Place): Result<Unit>
    suspend fun deleteAllFavouritePlaces(): Result<Unit>

    suspend fun getFavouriteRoutes(): Flow<Result<List<Route>>>
    suspend fun getFavouriteRoutesByWord(word: String): Flow<Result<List<Route>>>
    suspend fun addRouteToFavourite(route: Route): Result<Unit>
    suspend fun isFavouriteRoute(route: Route): Flow<Result<Boolean>>
    suspend fun removeRouteFromFavourite(route: Route): Result<Unit>
    suspend fun deleteAllFavouriteRoutes(): Result<Unit>
}