package ru.nn.tripnn.data.datasource.favourite

import kotlinx.coroutines.flow.Flow
import ru.nn.tripnn.data.database.place.favourite.FavouritePlace
import ru.nn.tripnn.data.database.route.RouteReference
import ru.nn.tripnn.data.database.route.favourite.FavouriteRoute

interface FavouritesDataSource {
    fun getAllFavouritePlaces(): Flow<Result<List<FavouritePlace>>>
    suspend fun deleteAllFavouritePlaces(): Result<Unit>
    suspend fun removePlaceFromFavourite(id: String): Result<Unit>
    suspend fun addPlaceToFavourite(id: String): Result<Unit>
    fun isPlaceFavourite(id: String): Flow<Result<Boolean>>

    fun getAllFavouriteRoutes(): Flow<Result<List<FavouriteRoute>>>
    suspend fun deleteAllFavouriteRoutes(): Result<Unit>
    suspend fun removeRouteFromFavourite(route: RouteReference): Result<Unit>
    suspend fun addRouteToFavourite(route: RouteReference): Result<Unit>
    fun isRouteFavourite(route: RouteReference): Flow<Result<Boolean>>
}