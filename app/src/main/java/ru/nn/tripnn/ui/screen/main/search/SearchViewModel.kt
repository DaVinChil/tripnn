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
import ru.nn.tripnn.domain.model.Place
import ru.nn.tripnn.domain.model.SearchFilters
import ru.nn.tripnn.data.remote.place.PlaceRepository
import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.ui.screen.authentication.ResourceState
import javax.inject.Inject

@HiltViewModel
class AllPlacesViewModel @Inject constructor(
    @Fake private val placeRepository: PlaceRepository
) : ViewModel() {

    var searchResult by mutableStateOf(ResourceState<List<Place>>())
        private set
    private var savedSearchResult: List<Place> by mutableStateOf(immutableListOf())

    fun search(searchFilters: SearchFilters) {
        viewModelScope.launch {
            searchResult = searchResult.copy(isLoading = true)
            when (val resource = placeRepository.find(searchFilters)) {
                is RemoteResource.Success -> {
                    searchResult = searchResult.copy(value = resource.data)
                    savedSearchResult = resource.data ?: listOf()
                }

                is RemoteResource.Error -> {
                    searchResult = searchResult.copy(
                        value = null,
                        error = resource.message,
                        isError = true,
                    )

                    savedSearchResult = listOf()
                }
            }
            searchResult = searchResult.copy(isLoading = false)
        }
    }

    fun sort(sortState: SortState) {
        viewModelScope.launch {
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
            searchResult = searchResult.copy(value = sorted)
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
    val byPrice: Boolean = false,
    val byRating: Boolean = false,
    val word: (String)? = null
)