package ru.nn.tripnn.data.datasource.localroute

import kotlinx.coroutines.CoroutineDispatcher
import ru.nn.tripnn.data.database.route.localroute.LocalRoute
import ru.nn.tripnn.data.database.route.localroute.LocalRouteDao
import ru.nn.tripnn.data.datasource.AbstractDataSource

class LocalRouteDataSourceImpl(
    private val localRouteDao: LocalRouteDao,
    ioDispatcher: CoroutineDispatcher
) : LocalRouteDataSource, AbstractDataSource(ioDispatcher) {
    override suspend fun findById(id: Long): Result<LocalRoute> = dispatchedRequest {
        localRouteDao.findById(id) ?: throw NoSuchElementException()
    }

    override suspend fun findByIds(ids: List<Long>): Result<List<LocalRoute>> = dispatchedRequest {
        buildList {
            for (id in ids) {
                add(findById(id).getOrThrow())
            }
        }
    }

    override suspend fun saveRoute(localRoute: LocalRoute): Result<Long> = dispatchedRequest {
        localRouteDao.saveRoute(route = localRoute)
    }

    override suspend fun rateRoute(rating: Int, routeId: Long): Result<Unit> = dispatchedRequest {
        localRouteDao.rateRoute(rating, routeId)
    }
}