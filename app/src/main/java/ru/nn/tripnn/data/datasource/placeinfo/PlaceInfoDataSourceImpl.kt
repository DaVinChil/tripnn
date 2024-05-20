package ru.nn.tripnn.data.datasource.placeinfo

import kotlinx.coroutines.CoroutineDispatcher
import ru.nn.tripnn.data.api.PlaceInfoApi
import ru.nn.tripnn.data.datasource.AbstractDataSource
import ru.nn.tripnn.data.dto.PlaceDto
import ru.nn.tripnn.data.dto.SearchFiltersDto
import ru.nn.tripnn.data.getOrThrow
import ru.nn.tripnn.domain.SearchFilters
import ru.nn.tripnn.domain.Sort

class PlaceInfoDataSourceImpl(
    private val placeInfoApi: PlaceInfoApi,
    ioDispatcher: CoroutineDispatcher
) : PlaceInfoDataSource, AbstractDataSource(ioDispatcher) {

    override suspend fun findById(id: String): Result<PlaceDto> = dispatchedRequest {
        placeInfoApi.searchById(id).getOrThrow()[0]
    }

    override suspend fun findByIds(ids: List<String>): Result<List<PlaceDto>> = dispatchedRequest {
        placeInfoApi.searchById(*ids.toTypedArray()).getOrThrow()
    }

    override suspend fun find(
        searchState: SearchFilters,
        page: Int?,
        pageSize: Int
    ): Result<List<PlaceDto>> = dispatchedRequest {
        val (lon, lat) = getLonAndLatOrNull(searchState.previousPlaceId)

        val dto = searchState.toDto(lon, lat, page, pageSize)
        placeInfoApi.search(
            locale = dto.locale,
            isWorkingNow = dto.isWorkingNow,
            priceRange = dto.priceRange,
            maxDistance = dto.maxDistance,
            minRating = dto.minRating,
            sort = dto.sort,
            page = dto.page,
            word = dto.word,
            lon = dto.lon,
            lat = dto.lat,
            types = dto.types
        ).getOrThrow()
    }

    private suspend fun getLonAndLatOrNull(prevPlaceId: String?): Pair<Double?, Double?> {
        if (prevPlaceId == null) return Pair(null, null)

        val prevPlace = placeInfoApi.searchById(prevPlaceId).getOrThrow()[0]
        return Pair(prevPlace.lon, prevPlace.lat)
    }

    private fun SearchFilters.toDto(
        lon: Double?,
        lat: Double?,
        page: Int?,
        pageSize: Int
    ): SearchFiltersDto {
        return SearchFiltersDto(
            locale = "ru",
            word = if (word?.length != 0) word else null,
            page = page,
            sort = when (sortBy) {
                Sort.RELEVANCE -> "relevance"
                Sort.DISTANCE -> "distance"
            },
            lon = lon?.toString(),
            lat = lat?.toString(),
            minRating = minRating,
            isWorkingNow = isWorkingNow,
            priceRange = if (price == -1) 0 else price,
            maxDistance = if (maxDistance == 0) null else maxDistance,
            types = types.map { it.id }
        )
    }
}