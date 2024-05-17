package ru.nn.tripnn.data.database.route.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.nn.tripnn.data.database.route.RouteReference
import java.util.Date

@Entity(tableName = "routes_history")
data class TakenRoute (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val remoteId: Long? = null,
    val localId: Long? = null,
    val wasTakenAt: Date
) : RouteReference {
    override fun localRouteId() = localId
    override fun remoteRouteId() = remoteId
}