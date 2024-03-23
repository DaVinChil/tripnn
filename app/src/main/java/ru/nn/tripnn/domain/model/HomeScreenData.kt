package ru.nn.tripnn.domain.model

data class HomeScreenData(
    val recommendedRoutes: List<Route> = listOf(),
    val curRoutePercent: Int? = null
)
