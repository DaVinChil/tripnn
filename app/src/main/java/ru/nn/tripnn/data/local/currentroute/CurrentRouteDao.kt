package ru.nn.tripnn.data.local.currentroute

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.nn.tripnn.domain.CurrentRoute

@Dao
interface CurrentRouteDao {
    @Query("select * from current_route limit 1")
    fun getCurrentRoute(): CurrentRoute?

    @Insert(entity = CurrentRoute::class, onConflict = OnConflictStrategy.REPLACE)
    fun saveCurrentRoute(currentRoute: CurrentRoute)

    @Query("delete from current_route")
    fun deleteCurrentRoute()
}