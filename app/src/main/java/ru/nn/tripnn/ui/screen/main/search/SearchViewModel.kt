package ru.nn.tripnn.ui.screen.main.search

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import ru.nn.tripnn.R
import ru.nn.tripnn.data.remote.place.PlaceRepository
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.SearchFilters
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.util.resourceStateFromRequest
import javax.inject.Inject

@HiltViewModel
class AllPlacesViewModel @Inject constructor(
    @Fake private val placeRepository: PlaceRepository
) : ViewModel() {

    var searchResult by mutableStateOf(ResourceState<List<Place>>())
        private set
    private var savedSearchResult: List<Place> by mutableStateOf(immutableListOf())

    private var searchFilters: SearchFilters = SearchFilters()

    fun search(searchFilters: SearchFilters) {
        viewModelScope.launch {
            this@AllPlacesViewModel.searchFilters = searchFilters
            resourceStateFromRequest { placeRepository.find(searchFilters) }.collectLatest {
                searchResult = it
                savedSearchResult = searchResult.value ?: listOf()
            }
        }
    }

    fun sort(sortState: SortState) {
        viewModelScope.launch {
            searchFilters = if (sortState.closer) {
                searchFilters.copy(sortBy = "distance")
            } else {
                searchFilters.copy(sortBy = "relevance")
            }

            resourceStateFromRequest { placeRepository.find(searchFilters) }.collectLatest {
                searchResult = it
                savedSearchResult = searchResult.value ?: listOf()
            }
        }
    }

    fun removeFromFavourite(id: String) {
        viewModelScope.launch {
            placeRepository.removeFromFavourite(id)
        }
    }

    fun addToFavourite(id: String) {
        viewModelScope.launch {
            placeRepository.addToFavourite(id)
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