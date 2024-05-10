package ru.nn.tripnn.data.local.favourite.route

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavouriteRoutesDao {
    @Query("select * from favourite_routes")
    fun getRoutes(): List<FavouriteRoute>

    @Query("select * from routes_history where routeId = :routeId")
    fun findByRouteId(routeId: String): FavouriteRoute?

    @Query("select * from routes_history where id = :id")
    fun findById(id: Int): FavouriteRoute?

    @Insert
    fun saveRoute(route: FavouriteRoute): Int

    @Query("delete from favourite_routes where id = :id")
    fun deleteRouteById(id: Int)

    @Query("delete from favourite_routes where routeId = :routeId")
    fun deleteRouteByRouteId(routeId: String)

    @Query("delete from favourite_routes")
    fun deleteAllFavouriteRoutes()
}