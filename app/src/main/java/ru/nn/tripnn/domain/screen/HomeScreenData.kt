package ru.nn.tripnn.domain.screen

import ru.nn.tripnn.domain.entity.Route

data class HomeScreenData(
    val recommendedRoutes: List<Route>,
    val curRoutePercent: Int
)
