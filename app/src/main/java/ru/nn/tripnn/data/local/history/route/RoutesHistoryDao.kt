package ru.nn.tripnn.data.local.history.route

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoutesHistoryDao {
    @Query("select * from routes_history")
    fun getRoutes(): List<HistoryRoute>

    @Query("select count(*) from routes_history where routeId = :routeId")
    fun hasByRouteId(routeId: String): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun saveRoute(historyRoute: HistoryRoute)

    @Delete
    fun deleteRoute(historyRoute: HistoryRoute)

    @Query("delete from routes_history")
    fun clearRoutesHistory()
}