package ru.nn.tripnn.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.nn.tripnn.data.dto.PlaceDto

interface PlaceInfoApi {
    @GET("search")
    suspend fun search(
        @Query("locale") locale: String,
        @Query("sort") sort: String,
        @Query("page") page: Int?,
        @Query("word") word: String?,
        @Query("lon") lon: String?,
        @Query("lat") lat: String?,
        @Query("types") types: List<Int>,
        @Query("minRating") minRating: Float,
        @Query("priceRange") priceRange: Int,
        @Query("maxDistance") maxDistance: Int?,
        @Query("isWorkingNow") isWorkingNow: Boolean
    ): Response<List<PlaceDto>>

    @GET("search/byid")
    suspend fun searchById(@Query("ids") vararg ids: String): Response<List<PlaceDto>>
}