package ru.nn.tripnn.data.datasource.currentroute

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.nn.tripnn.data.database.currentroute.CurrentRouteDao
import ru.nn.tripnn.data.database.currentroute.CurrentRouteEntity
import ru.nn.tripnn.data.datasource.AbstractDataSource
import ru.nn.tripnn.data.toResultFlow

class CurrentRouteDataSourceImpl(
    private val currentRouteDao: CurrentRouteDao,
    ioDispatcher: CoroutineDispatcher
) : CurrentRouteDataSource, AbstractDataSource(ioDispatcher) {
    override fun getCurrentRoute(): Flow<Result<CurrentRouteEntity?>> {
        return currentRouteDao.getCurrentRoute().toResultFlow().map { it.also { println("new route -> ${it.getOrNull()}") } }
    }

    override suspend fun deleteCurrentRoute(): Result<Unit> = dispatchedRequest {
        currentRouteDao.deleteCurrentRoute()
    }

    override suspend fun createNewCurrentRoute(): Result<Unit> = dispatchedRequest {
        currentRouteDao.saveCurrentRoute(CurrentRouteEntity())
    }

    override suspend fun addPlaceToRoute(id: String): Result<Unit> = dispatchedRequest {
        getCurrentRoute().first().getOrThrow()?.let { route ->
            val newPlaces = route.places.toMutableList().also { it.add(id) }

            currentRouteDao.saveCurrentRoute(route.copy(places = newPlaces))
        }
    }

    override suspend fun removePlaceFromRoute(index: Int): Result<Unit> = dispatchedRequest {
        getCurrentRoute().first().getOrThrow()?.let { route ->
            val newPlaces = route.places.toMutableList().also { it.removeAt(index) }
            currentRouteDao.saveCurrentRoute(route.copy(places = newPlaces))
        }
    }

    override suspend fun clearCurrentRoute(): Result<Unit> = dispatchedRequest {
        currentRouteDao.deleteCurrentRoute()
    }

    override suspend fun takeCurrentRoute(): Result<Unit> = dispatchedRequest {
        currentRouteDao.setBuildNotInProgress()
    }

    override suspend fun goToNextPlace(): Result<Unit> = dispatchedRequest {
        currentRouteDao.nextPlace()
    }

    override suspend fun setCurrentRoute(currentRouteEntity: CurrentRouteEntity): Result<Unit> = dispatchedRequest {
        currentRouteDao.saveCurrentRoute(currentRouteEntity)
    }
}