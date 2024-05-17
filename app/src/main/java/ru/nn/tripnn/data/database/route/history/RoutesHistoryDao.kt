package ru.nn.tripnn.data.database.route.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutesHistoryDao {
    @Query("select * from routes_history")
    fun getRoutes(): Flow<List<TakenRoute>?>

    @Query("select * from routes_history where remoteId = :remoteId limit 1")
    fun findByRemoteId(remoteId: Long): Flow<TakenRoute>

    @Query("select * from routes_history where localId = :localId limit 1")
    fun findByLocalId(localId: Long): Flow<TakenRoute>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveRoute(takenRoute: TakenRoute)

    @Query("delete from routes_history where localId = :localId")
    fun deleteByLocalId(localId: Long)

    @Query("delete from routes_history where remoteId = :remoteId")
    fun deleteByRemoteId(remoteId: Long)

    @Query("delete from routes_history")
    fun clearRoutesHistory()
}