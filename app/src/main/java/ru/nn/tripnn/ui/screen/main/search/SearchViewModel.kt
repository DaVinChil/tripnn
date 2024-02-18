package ru.nn.tripnn.ui.screen.main.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.repository.PlaceRepository
import ru.nn.tripnn.domain.util.Resource
import ru.nn.tripnn.ui.screen.main.favourite.ResourceListState
import javax.inject.Inject

@HiltViewModel
class AllPlacesViewModel @Inject constructor(
    @Fake private val placeRepository: PlaceRepository
) : ViewModel() {

    var searchResult by mutableStateOf(ResourceListState<Place>())
        private set
    private var savedSearchResult: List<Place> by mutableStateOf(immutableListOf())

    fun search(searchState: SearchState) {
        viewModelScope.launch {
            searchResult = searchResult.copy(isLoading = true)
            when (val resource = placeRepository.find(searchState)) {
                is Resource.Success -> {
                    searchResult = searchResult.copy(list = resource.data ?: listOf())
                    savedSearchResult = searchResult.list
                }

                is Resource.Error -> {

                }
            }
            searchResult = searchResult.copy(isLoading = true)
        }
    }

    fun sort(sortState: SortState) {
        val sorted = mutableListOf<Place>()
        savedSearchResult.forEach {
            if (sortState.word.isNullOrBlank() ||
                it.name.contains(
                    sortState.word,
                    ignoreCase = true
                )
            ) sorted.add(it)
        }
        sorted.sortWith { a, b ->
            if (sortState.byRating && (a.rating != b.rating)) {
                (a.rating - b.rating).toInt()
            } else if (sortState.byPrice && (a.cost != b.cost)) {
                a.cost.toInt() - b.cost.toInt()
            }
            0
        }
        searchResult = searchResult.copy(list = sorted)
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

data class SearchState(
    val input: String,
    val types: List<Int>,
    val catalog: String,
    val priceRange: ClosedFloatingPointRange<Float>
)

data class SortState(
    val byPrice: Boolean = false,
    val byRating: Boolean = false,
    val word: (String)? = null
)