package ru.nn.tripnn.data.database.currentroute

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentRouteDao {
    @Query("select * from current_route limit 1")
    fun getCurrentRoute(): Flow<CurrentRouteEntity?>

    @Insert(entity = CurrentRouteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun saveCurrentRoute(currentRoute: CurrentRouteEntity)

    @Query("update current_route set currentPlaceIndex = currentPlaceIndex + 1")
    fun nextPlace()

    @Query("update current_route set buildInProgress = false")
    fun setBuildNotInProgress()

    @Query("delete from current_route")
    fun deleteCurrentRoute()

    @Query("update current_route set finished = true")
    fun setFinished()
}