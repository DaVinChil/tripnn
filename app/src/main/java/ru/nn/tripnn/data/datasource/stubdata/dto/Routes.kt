package ru.nn.tripnn.data.datasource.stubdata.dto

import ru.nn.tripnn.data.database.currentroute.CurrentRouteEntity
import ru.nn.tripnn.data.dto.RouteDto

val DTO_ROUTE_1 = RouteDto(
    id = 1,
    title = "Историческая часть города",
    image = "https://cdn.culture.ru/images/80aee2a7-ead2-5308-a550-b2c49109f906",
    places = DTO_PLACES,
    desc = "Рекомендованное время начала не позднее 18:00"
)

val DTO_ROUTES = listOf(DTO_ROUTE_1, DTO_ROUTE_1.copy(id = 2), DTO_ROUTE_1.copy(id = 3))

val CURRENT_ROUTE = CurrentRouteEntity(
    places = listOf("1", "1", "1"),
    currentPlaceIndex = 0,
    buildInProgress = false
)