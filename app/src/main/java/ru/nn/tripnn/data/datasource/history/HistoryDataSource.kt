package ru.nn.tripnn.data.datasource.history

import kotlinx.coroutines.flow.Flow
import ru.nn.tripnn.data.database.place.history.VisitedPlace
import ru.nn.tripnn.data.database.route.RouteReference
import ru.nn.tripnn.data.database.route.history.TakenRoute
import java.util.Date

interface HistoryDataSource {
    fun getAllVisitedPlaces(): Flow<Result<List<VisitedPlace>>>
    suspend fun deleteAllVisitedPlaces(): Result<Unit>
    fun isPlaceVisitedById(id: String): Flow<Result<Boolean>>
    suspend fun addPlaceToHistory(id: String): Result<Unit>
    suspend fun removePlaceFromHistory(id: String): Result<Unit>

    fun getTakenRoutes(): Flow<Result<List<TakenRoute>>>
    suspend fun deleteAllTakenRoutes(): Result<Unit>
    fun whenRouteWasTaken(route: RouteReference): Flow<Result<Date?>>
    suspend fun addRouteToHistory(route: RouteReference): Result<Unit>
    suspend fun removeRouteFromHistory(route: RouteReference): Result<Unit>
}