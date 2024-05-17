package ru.nn.tripnn.data.datasource.favourite

import ru.nn.tripnn.data.database.place.favourite.FavouritePlace
import ru.nn.tripnn.data.database.route.RouteReference
import ru.nn.tripnn.data.database.route.favourite.FavouriteRoute

fun favouritePlace(placeId: String): FavouritePlace {
    return FavouritePlace(placeId = placeId)
}

fun favouriteRoute(routeReference: RouteReference): FavouriteRoute {
    return FavouriteRoute(
        localId = routeReference.localRouteId(),
        remoteId = routeReference.remoteRouteId()
    )
}