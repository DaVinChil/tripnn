package ru.nn.tripnn.data.api

import retrofit2.Response
import retrofit2.http.GET
import ru.nn.tripnn.data.dto.RouteDto

interface RecommendedRoutesApi {
    @GET("routes")
    suspend fun getRoutes(): Response<List<RouteDto>>
}