package ru.nn.tripnn.data.database.route.favourite

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteRoutesDao {
    @Query("select * from favourite_routes")
    fun getRoutes(): Flow<List<FavouriteRoute>>

    @Query("select * from favourite_routes where remoteId = :remoteId limit 1")
    fun findByRemoteId(remoteId: Long): Flow<FavouriteRoute>

    @Query("select * from favourite_routes where localId = :localId limit 1")
    fun findByLocalId(localId: Long): Flow<FavouriteRoute>

    @Insert
    fun saveRoute(route: FavouriteRoute): Long

    @Query("delete from favourite_routes where localId = :localId")
    fun deleteByLocalId(localId: Long)

    @Query("delete from favourite_routes where remoteId = :remoteId")
    fun deleteByRemoteId(remoteId: Long)

    @Query("delete from favourite_routes")
    fun deleteAllFavouriteRoutes()
}