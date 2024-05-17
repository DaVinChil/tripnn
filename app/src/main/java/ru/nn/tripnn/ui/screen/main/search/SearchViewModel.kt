package ru.nn.tripnn.ui.screen.main.search

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.nn.tripnn.R
import ru.nn.tripnn.data.repository.favourite.FavouritesRepository
import ru.nn.tripnn.data.repository.searchplace.SearchPlaceService
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.SearchFilters
import ru.nn.tripnn.ui.util.toResourceStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPlaceService: SearchPlaceService,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {
    private var searchFilters = MutableStateFlow(SearchFilters())

    @OptIn(ExperimentalCoroutinesApi::class)
    var searchResult = searchFilters.flatMapLatest { searchPlaceService.find(it) }
        .toResourceStateFlow(viewModelScope)

    fun search(newSearchFilters: SearchFilters) {
        viewModelScope.launch {
            searchFilters.emit(newSearchFilters)
        }
    }

    fun sort(sortState: SortState) {
        viewModelScope.launch {
            val value = searchFilters.value

            val newFilters = if (sortState.closer) {
                value.copy(word = sortState.word, sortBy = "distance")
            } else if (sortState.byRating) {
                value.copy(word = sortState.word, sortBy = "relevance")
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
    Type(R.string.cinemas, 0),
//    Type(R.string.bowling, 1),
//    Type(R.string.nightclub, 2),
//    Type(R.string.billiard, 3),
    Type(R.string.karaoke, 4),
//    Type(R.string.shooting_club, 5),
//    Type(R.string.golf_club, 6),
//    Type(R.string.carting, 7),
    Type(R.string.circuses, 8),
//    Type(R.string.skating_rinks, 9),
    Type(R.string.zoos, 11),
    Type(R.string.anti_cafe, 13),
    Type(R.string.water_parks, 15),
//    Type(R.string.trampoline_centre, 16),
    Type(R.string.attractions, 17),
)
val CULTURE_TYPES = listOf(
    Type(R.string.museums, 33),
    Type(R.string.parks, 34)
)
val EAT_TYPES = listOf(
    Type(R.string.sushi_bars, 27),
    Type(R.string.pizzerias, 28),
    Type(R.string.fast_food, 26),
    Type(R.string.canteens, 25),
    Type(R.string.coffee_shops, 24),
    Type(R.string.bars, 159),
    Type(R.string.pastry_shops, 29),
    Type(R.string.shot_glasses, 30),
)