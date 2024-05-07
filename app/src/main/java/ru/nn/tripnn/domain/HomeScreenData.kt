package ru.nn.tripnn.domain

data class HomeScreenData(
    val recommendedRoutes: List<Route> = listOf(),
    val curRoutePercent: Int? = null
)
