package ru.nn.tripnn.data.database.route.favourite

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.nn.tripnn.data.database.route.RouteReference

@Entity(tableName = "favourite_routes")
data class FavouriteRoute (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val localId: Long? = null,
    val remoteId: Long? = null
) : RouteReference {
    override fun localRouteId() = localId
    override fun remoteRouteId() = remoteId
}