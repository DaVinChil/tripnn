package ru.nn.tripnn.data.repository.favourite

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.nn.tripnn.data.database.place.favourite.FavouritePlace
import ru.nn.tripnn.data.datasource.favourite.FavouritesDataSource
import ru.nn.tripnn.data.repository.aggregator.PlaceDataAggregator
import ru.nn.tripnn.data.repository.aggregator.RouteDataAggregator
import ru.nn.tripnn.data.toResultFlow
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route

class FavouritesRepositoryImpl(
    private val favouritesDataSource: FavouritesDataSource,
    private val placeDataAggregator: PlaceDataAggregator,
    private val routeDataAggregator: RouteDataAggregator
) : FavouritesRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getFavouritePlaces(): Flow<Result<List<Place>>> {
        val ids = favouritesDataSource.getAllFavouritePlaces()
        return ids.flatMapLatest {
            placeDataAggregator.placesFromIds(
                it.getOrThrow().map(FavouritePlace::placeId)
            )
        }
    }

    override suspend fun getFavouritePlacesByWord(word: String): Flow<Result<List<Place>>> {
        val favouritesFlow = getFavouritePlaces()

        return if (word.isNotEmpty()) {
            favouritesFlow
                .map { it.getOrThrow().filter { place -> place.name.contains(word) } }
                .toResultFlow()
        } else {
            favouritesFlow
        }
    }

    override suspend fun addPlaceToFavourite(place: Place): Result<Unit> {
        return favouritesDataSource.addPlaceToFavourite(place.id)
    }

    override suspend fun isFavouritePlace(place: Place): Flow<Result<Boolean>> {
        return favouritesDataSource.isPlaceFavourite(place.id)
    }

    override suspend fun removePlaceFromFavourite(place: Place): Result<Unit> {
        return favouritesDataSource.removePlaceFromFavourite(place.id)
    }

    override suspend fun deleteAllFavouritePlaces(): Result<Unit> {
        return favouritesDataSource.deleteAllFavouritePlaces()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getFavouriteRoutes(): Flow<Result<List<Route>>> {
        val routes = favouritesDataSource.getAllFavouriteRoutes()
        return routes.flatMapLatest {
            routeDataAggregator.routes(it.getOrThrow())
        }
    }

    override suspend fun getFavouriteRoutesByWord(word: String): Flow<Result<List<Route>>> {
        val favouritesFlow = getFavouriteRoutes()

        return if (word.isNotEmpty()) {
            favouritesFlow
                .map { it.getOrThrow().filter { route -> route.title.contains(word) } }
                .toResultFlow()
        } else {
            favouritesFlow
        }
    }

    override suspend fun addRouteToFavourite(route: Route): Result<Unit> {
        return favouritesDataSource.addRouteToFavourite(route)
    }

    override suspend fun isFavouriteRoute(route: Route): Flow<Result<Boolean>> {
        return favouritesDataSource.isRouteFavourite(route)
    }

    override suspend fun removeRouteFromFavourite(route: Route): Result<Unit> {
        return favouritesDataSource.removeRouteFromFavourite(route)
    }

    override suspend fun deleteAllFavouriteRoutes(): Result<Unit> {
        return favouritesDataSource.deleteAllFavouriteRoutes()
    }
}