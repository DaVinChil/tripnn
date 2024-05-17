package ru.nn.tripnn.data.repository.history

import kotlinx.coroutines.flow.Flow
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route

interface HistoryRepository {
    suspend fun getTakenRoutes(): Flow<Result<List<Route>>>
    suspend fun getTakenRoutesByWord(word: String): Flow<Result<List<Route>>>
    suspend fun addRouteToHistory(route: Route): Result<Unit>
    suspend fun removeRouteFromHistory(route: Route): Result<Unit>
    suspend fun clearRoutesHistory(): Result<Unit>

    suspend fun getVisitedPlaces(): Flow<Result<List<Place>>>
    suspend fun getVisitedPlacesByWord(word: String): Flow<Result<List<Place>>>
    suspend fun addPlaceToHistory(place: Place): Result<Unit>
    suspend fun removePlaceFromHistory(place: Place): Result<Unit>
    suspend fun clearPlacesHistory(): Result<Unit>
}