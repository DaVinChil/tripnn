package ru.nn.tripnn.data.datasource.currentroute

import kotlinx.coroutines.flow.Flow
import ru.nn.tripnn.data.database.currentroute.CurrentRouteEntity
import ru.nn.tripnn.data.database.route.RouteReference

interface CurrentRouteDataSource {
    fun getCurrentRoute(): Flow<Result<CurrentRouteEntity?>>
    suspend fun addRouteReference(routeReference: RouteReference): Result<Unit>
    suspend fun deleteCurrentRoute(): Result<Unit>
    suspend fun createNewCurrentRoute(): Result<Unit>
    suspend fun addPlaceToRoute(id: String): Result<Unit>
    suspend fun removePlaceFromRoute(index: Int): Result<Unit>
    suspend fun clearCurrentRoute(): Result<Unit>
    suspend fun takeCurrentRoute(): Result<Unit>
    suspend fun goToNextPlace(): Result<Unit>
    suspend fun setCurrentRoute(currentRouteEntity: CurrentRouteEntity): Result<Unit>
}