package ru.nn.tripnn.data.database.route.localroute

import androidx.room.Dao
import androidx.room.Query

@Dao
interface LocalRouteDao {
    @Query("select * from local_routes where id = :id")
    fun findById(id: Long): LocalRoute?
}