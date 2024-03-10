package ru.nn.tripnn.ui.screen.main.favourite

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
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.repository.PlaceRepository
import ru.nn.tripnn.domain.repository.RouteRepository
import ru.nn.tripnn.domain.util.RemoteResource
import ru.nn.tripnn.ui.screen.ResourceState
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
            favouritePlaces = favouritePlaces.copy(isLoading = true)

            when (val result = placeRepository.getFavourite()) {
                is RemoteResource.Success -> {
                    favouritePlaces = favouritePlaces.copy(
                        value = result.data,
                        error = null,
                        isError = false
                    )
                    savedFavouritePlaces = result.data ?: listOf()
                }

                is RemoteResource.Error -> {
                    favouritePlaces = favouritePlaces.copy(
                        value = null,
                        error = result.message,
                        isError = true
                    )
                }
            }

            favouritePlaces = favouritePlaces.copy(isLoading = false)
        }
    }

    private fun loadFavouriteRoutes() {
        viewModelScope.launch {
            favouriteRoutes = favouriteRoutes.copy(isLoading = true)
            when (val result = routeRepository.getFavourite()) {
                is RemoteResource.Success -> {
                    favouriteRoutes = favouriteRoutes.copy(
                        value = result.data,
                        error = null,
                        isError = false
                    )
                    savedFavouriteRoutes = result.data ?: listOf()
                }

                is RemoteResource.Error -> {
                    favouriteRoutes = favouriteRoutes.copy(
                        value = null,
                        error = result.message,
                        isError = true
                    )

                    savedFavouriteRoutes = listOf()
                }
            }

            favouriteRoutes = favouriteRoutes.copy(isLoading = false)
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
            if (word.isBlank() || it.name.contains(word, ignoreCase = true)) {
                filtered.add(it)
            }
        }
        favouriteRoutes = favouriteRoutes.copy(value = filtered)
    }

    fun filterPlaces(word: String) {
        val filtered = mutableListOf<Place>()
        savedFavouritePlaces.forEach {
            if (word.isBlank() || it.name.contains(word, ignoreCase = true)) {
                filtered.add(it)
            }
        }
        favouritePlaces = favouritePlaces.copy(value = filtered)
    }
}