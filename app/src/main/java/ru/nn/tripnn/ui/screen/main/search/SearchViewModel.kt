package ru.nn.tripnn.ui.screen.main.search

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.nn.tripnn.R
import ru.nn.tripnn.data.repository.favourite.FavouritesRepository
import ru.nn.tripnn.data.repository.searchplace.SearchPlaceService
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.SearchFilters
import ru.nn.tripnn.domain.Sort
import ru.nn.tripnn.domain.state.ResState
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPlaceService: SearchPlaceService,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {
    private var searchFilters = MutableStateFlow(SearchFilters())

    @OptIn(ExperimentalCoroutinesApi::class)
    private var _searchResult: Flow<PagingData<StateFlow<ResState<Place>>>> = searchFilters.flatMapLatest {
        searchPlaceService.find(it, viewModelScope)
            .flow
            .cachedIn(viewModelScope)
    }

    val searchResult @Composable get() = _searchResult.collectAsLazyPagingItems()

    fun search(newSearchFilters: SearchFilters) {
        viewModelScope.launch {
            searchFilters.emit(newSearchFilters)
        }
    }

    fun sort(sortState: SortState) {
        viewModelScope.launch {
            val value = searchFilters.value

            val newFilters = if (sortState.closer) {
                value.copy(word = sortState.word, sortBy = Sort.DISTANCE)
            } else if (sortState.byRating) {
                value.copy(word = sortState.word, sortBy = Sort.RELEVANCE)
            } else {
                value.copy(word = sortState.word)
            }

            searchFilters.emit(newFilters)
        }
    }

    fun removeFromFavourite(place: Place) {
        viewModelScope.launch {
            favouritesRepository.removePlaceFromFavourite(place)
        }
    }

    fun addToFavourite(place: Place) {
        viewModelScope.launch {
            favouritesRepository.addPlaceToFavourite(place)
        }
    }
}

data class SortState(
    val closer: Boolean = false,
    val byRating: Boolean = false,
    val word: (String)? = null
)

data class Type(
    @StringRes
    val resId: Int,
    val id: Int
)

val LEISURE_TYPES = listOf(
    Type(R.string.cinemas, 192),
//    Type(R.string.bowling, 1),
//    Type(R.string.nightclub, 2),
//    Type(R.string.billiard, 3),
    Type(R.string.karaoke, 21387),
//    Type(R.string.shooting_club, 5),
//    Type(R.string.golf_club, 6),
//    Type(R.string.carting, 7),
    Type(R.string.circuses, 685),
//    Type(R.string.skating_rinks, 9),
    Type(R.string.zoos, 167),
    Type(R.string.anti_cafe, 72370),
    Type(R.string.water_parks, 537),
//    Type(R.string.trampoline_centre, 16),
    Type(R.string.attractions, 110358),
)
val CULTURE_TYPES = listOf(
    Type(R.string.museums, 193),
    Type(R.string.parks, 168)
)
val EAT_TYPES = listOf(
    Type(R.string.sushi_bars, 15791),
    Type(R.string.pizzerias, 51459),
    Type(R.string.fast_food, 165),
    Type(R.string.canteens, 166),
    Type(R.string.coffee_shops, 162),
    Type(R.string.bars, 159),
    Type(R.string.pastry_shops, 112658),
    Type(R.string.shot_glasses, 52248),
)