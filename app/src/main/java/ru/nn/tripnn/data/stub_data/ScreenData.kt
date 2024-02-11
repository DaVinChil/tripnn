package ru.nn.tripnn.data.stub_data

import ru.nn.tripnn.domain.screen.HomeScreenData
import ru.nn.tripnn.domain.entity.UserInfo

val homeScreenData =
    HomeScreenData(
        recommendedRoutes = ROUTES,
        curRoutePercent = 33
    )

val userInfo =
    UserInfo(
        name = "Sasha",
        email = "hz.com@gmai.com",
        avatar = null
    )
