package ru.nn.tripnn.data.datasource.stubdata.dto

import ru.nn.tripnn.data.dto.PlaceDto

val DTO_PLACE_1 = PlaceDto(
    photos = listOf(
        "https://i9.photo.2gis.com/images/branch/0/30258560126650909_545f.jpg",
        "https://avatars.mds.yandex.net/get-altay/10447847/2a0000018afa0f65d8cbcc3f30398879460b/L_height",
        "https://avatars.mds.yandex.net/get-altay/9827997/2a0000018afa0f6216a04176cf65fc35d67a/L_height",
        "https://avatars.mds.yandex.net/get-altay/10830675/2a0000018afaa22a9aca249fb3c914067c9d/L_height"
    ),
    id = "1",
    cost = "150",
    rating = 4.9,
    name = "Вспышка",
    type = "Пышечная",
    address = "Алексеевская, 11",
    workTime = "10:00 - 21:00",
    phone = "+7-(963)-231-96-17",
    reviews = 199,
    doubleGisLink = "https://go.2gis.com/wrht0",
    lon = 44.005713,
    lat = 56.32716
)

val DTO_PLACES = listOf(DTO_PLACE_1, DTO_PLACE_1, DTO_PLACE_1)