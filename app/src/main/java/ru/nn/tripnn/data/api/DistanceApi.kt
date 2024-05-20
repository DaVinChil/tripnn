package ru.nn.tripnn.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.nn.tripnn.data.dto.DistanceDto

interface DistanceApi {
    @GET("distance")
    suspend fun getDistance(
        @Query("srcLon") srcLon: Double,
        @Query("srcLat") srcLat: Double,
        @Query("destLon") destLon: Double,
        @Query("destLat") destLat: Double
    ): Response<DistanceDto>
}