package ru.nn.tripnn.data.stub_data

import ru.nn.tripnn.domain.entity.Route

val ROUTE_1 = Route(id = "1", name = "Историческая часть города", cost = "0 - 500", imageUrl = "https://cdn.culture.ru/images/80aee2a7-ead2-5308-a550-b2c49109f906")

val ROUTES = listOf(ROUTE_1, ROUTE_1.copy(id = "2"), ROUTE_1.copy(id = "3"))