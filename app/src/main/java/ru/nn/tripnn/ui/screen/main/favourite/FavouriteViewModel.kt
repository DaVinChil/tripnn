package ru.nn.tripnn.ui.screen.main.favourite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import ru.nn.tripnn.data.stub_data.PLACE_1
import ru.nn.tripnn.data.stub_data.ROUTE_1
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.repository.PlaceRepository
import ru.nn.tripnn.domain.repository.RouteRepository
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    @Fake private val placeRepository: PlaceRepository,
    @Fake private val routeRepository: RouteRepository
) : ViewModel() {
    val isLoading
        get() = favouritePlaces.isLoading || favouriteRoutes.isLoading

    var favouritePlaces by mutableStateOf(ResourceListState<Place>())
    private var savedFavouritePlaces = immutableListOf<Place>()


    var favouriteRoutes by mutableStateOf(ResourceListState<Route>())
    private var savedFavouriteRoutes = immutableListOf<Route>()

    init {
        loadFavouritePlaces()
        loadFavouriteRoutes()
    }

    private fun loadFavouritePlaces() {
        viewModelScope.launch {
            favouritePlaces = favouritePlaces.copy(isLoading = true)
            when (val result = placeRepository.getFavourite()) {
                is Resource.Success -> {
                    favouritePlaces = favouritePlaces.copy(list = result.data ?: listOf())
                    savedFavouritePlaces = favouritePlaces.list ?: listOf()
                }

                is Resource.Error -> {

                }
            }
            favouritePlaces = favouritePlaces.copy(isLoading = false)
        }
    }

    private fun loadFavouriteRoutes() {
        viewModelScope.launch {
            favouriteRoutes = favouriteRoutes.copy(isLoading = true)
            when (val result = routeRepository.getFavourite()) {
                is Resource.Success -> {
                    favouriteRoutes = favouriteRoutes.copy(list = result.data ?: listOf())
                    savedFavouriteRoutes = favouriteRoutes.list
                }

                is Resource.Error -> {

                }
            }
            favouritePlaces = favouritePlaces.copy(isLoading = false)
        }
    }

    fun removePlaceFromFavourite(id: String) {
        viewModelScope.launch {
            placeRepository.removeFromFavourite(id)
        }
    }

    fun addPlaceToFavourite(id: String) {
        viewModelScope.launch {
            placeRepository.addToFavourite(id)
        }
    }

    fun removeRouteFromFavourite(id: String) {
        viewModelScope.launch {
            routeRepository.removeFromFavourite(id)
        }
    }

    fun addRouteToFavourite(id: String) {
        viewModelScope.launch {
            routeRepository.addToFavourite(id)
        }
    }

    fun filterRoutes(word: String) {
        val filtered = mutableListOf<Route>()
        savedFavouriteRoutes.forEach {
            if (word.isNullOrBlank() || it.name.contains(word, ignoreCase = true)) {
                filtered.add(it)
            }
        }
        favouriteRoutes = favouriteRoutes.copy(list = filtered)
    }

    fun filterPlaces(word: String) {
        val filtered = mutableListOf<Place>()
        savedFavouritePlaces.forEach {
            if (word.isNullOrBlank() || it.name.contains(word, ignoreCase = true)) {
                filtered.add(it)
            }
        }
        favouritePlaces = favouritePlaces.copy(list = filtered)
    }
}

data class ResourceListState<T>(
    val isLoading: Boolean = false,
    val error: String? = null,
    val list: List<T> = listOf()
)