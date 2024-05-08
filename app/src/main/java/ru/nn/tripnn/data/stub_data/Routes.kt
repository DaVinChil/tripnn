package ru.nn.tripnn.data.stub_data

import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.domain.Route
import java.util.Date

val ROUTE_1 = Route(
    id = "1",
    name = "Историческая часть города",
    cost = "0 - 500",
    imageUrl = "https://cdn.culture.ru/images/80aee2a7-ead2-5308-a550-b2c49109f906",
    favourite = true,
    rating = 4.7,
    places = listOf(
        PLACE_1,
        PLACE_1.copy(id = "2"),
        PLACE_1.copy(id = "3")
    ),
    wasTaken = Date(),
    desc = "Рекомендованное время начала не позднее 18:00"
)

val ROUTES = listOf(ROUTE_1, ROUTE_1.copy(id = "2"), ROUTE_1.copy(id = "3"))

val CURRENT_ROUTE = CurrentRoute(
    places = listOf(PLACE_1, PLACE_1),
    currentPlaceIndex = 0,
    buildInProgress = false
)