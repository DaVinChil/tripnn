package ru.nn.tripnn.data.database.currentroute

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import ru.nn.tripnn.data.database.route.RouteReference

@Immutable
@Entity(tableName = "current_route")
data class CurrentRouteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val places: List<String> = listOf(),
    val currentPlaceIndex: Int = 0,
    val buildInProgress: Boolean = true,
    val remoteRouteId: Long? = null,
    val localRouteId: Long? = null,
    val finished: Boolean = false
) : RouteReference {
    @Ignore
    override fun localRouteId(): Long? {
        return localRouteId
    }

    @Ignore
    override fun remoteRouteId(): Long? {
        return remoteRouteId
    }

}
