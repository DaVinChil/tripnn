package ru.nn.tripnn.data.database.route.localroute

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface LocalRouteDao {
    @Query("select * from local_routes where id = :id")
    fun findById(id: Long): LocalRoute?

    @Insert
    fun saveRoute(route: LocalRoute): Long

    @Query("update local_routes set rating = :rating where id = :routeId")
    fun rateRoute(rating: Int, routeId: Long)
}