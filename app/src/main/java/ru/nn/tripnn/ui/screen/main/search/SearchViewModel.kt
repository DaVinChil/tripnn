package ru.nn.tripnn.ui.screen.main.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
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

    var searchFilters: SearchFilters = SearchFilters()
        private set

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
            searchFilters = searchFilters.copy(
                sortByDistance = sortState.closer,
                sortByRating = sortState.byRating
            )
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