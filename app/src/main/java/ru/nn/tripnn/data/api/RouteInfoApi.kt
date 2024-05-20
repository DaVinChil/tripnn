package ru.nn.tripnn.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.nn.tripnn.data.dto.RouteDto

interface RouteInfoApi {
    @GET("routes/byid")
    suspend fun searchRouteById(@Query("id") id: Long): Response<RouteDto>
}