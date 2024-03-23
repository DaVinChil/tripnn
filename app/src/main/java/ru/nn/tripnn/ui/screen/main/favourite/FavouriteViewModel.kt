package ru.nn.tripnn.ui.screen.main.favourite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.model.Place
import ru.nn.tripnn.domain.model.Route
import ru.nn.tripnn.data.remote.place.PlaceRepository
import ru.nn.tripnn.data.remote.route.RouteRepository
import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.ui.screen.authentication.ResourceState
import ru.nn.tripnn.ui.util.resourceStateFromRequest
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    @Fake private val placeRepository: PlaceRepository,
    @Fake private val routeRepository: RouteRepository
) : ViewModel() {

    var favouritePlaces by mutableStateOf(ResourceState<List<Place>>())
    private var savedFavouritePlaces = immutableListOf<Place>()

    var favouriteRoutes by mutableStateOf(ResourceState<List<Route>>())
    private var savedFavouriteRoutes = immutableListOf<Route>()

    fun init() {
        loadFavouritePlaces()
        loadFavouriteRoutes()
    }

    private fun loadFavouritePlaces() {
        viewModelScope.launch {
            if (savedFavouritePlaces.isNotEmpty()) {
                favouritePlaces = favouritePlaces.copy(value = savedFavouritePlaces)
            }

            resourceStateFromRequest(placeRepository::getFavourite).collectLatest {
                favouritePlaces = it
                savedFavouritePlaces = it.value ?: listOf()
            }
        }
    }

    private fun loadFavouriteRoutes() {
        viewModelScope.launch {
            if (savedFavouriteRoutes.isNotEmpty()) {
                favouriteRoutes = favouriteRoutes.copy(value = savedFavouriteRoutes)
            }

            resourceStateFromRequest(routeRepository::getFavourite).collectLatest {
                favouriteRoutes = it
                savedFavouriteRoutes = it.value ?: listOf()
            }
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
        val filtered = filterByWord(word, savedFavouriteRoutes, Route::name)
        favouriteRoutes = favouriteRoutes.copy(value = filtered)
    }

    fun filterPlaces(word: String) {
        val filtered = filterByWord(word, savedFavouritePlaces, Place::name)
        favouritePlaces = favouritePlaces.copy(value = filtered)
    }

    private fun <T> filterByWord(word: String, list: List<T>, getName: T.() -> String): List<T> {
        val filtered = mutableListOf<T>()
        list.forEach {
            if (word.isBlank() || it.getName().contains(word, ignoreCase = true)) {
                filtered.add(it)
            }
        }

        return filtered
    }
}