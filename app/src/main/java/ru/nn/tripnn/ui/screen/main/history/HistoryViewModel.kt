package ru.nn.tripnn.ui.screen.main.history

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
import ru.nn.tripnn.domain.repository.HistoryRepository
import ru.nn.tripnn.domain.repository.PlaceRepository
import ru.nn.tripnn.domain.repository.RouteRepository
import ru.nn.tripnn.domain.util.RemoteResource
import ru.nn.tripnn.ui.screen.ResourceState
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    @Fake private val historyRepository: HistoryRepository,
    @Fake private val placeRepository: PlaceRepository,
    @Fake private val routeRepository: RouteRepository
) : ViewModel() {

    var visitedPlaces by mutableStateOf(ResourceState<List<Place>>())
    private var savedPlaces = immutableListOf<Place>()

    var takenRoutes by mutableStateOf(ResourceState<List<Route>>())
    private var savedRoutes = immutableListOf<Route>()

    fun init() {
        loadVisitedPlaces()
        loadTakenRoutes()
    }

    private fun loadVisitedPlaces() {
        viewModelScope.launch {
            visitedPlaces = visitedPlaces.copy(isLoading = true)
            when (val result = historyRepository.getPlaces()) {
                is RemoteResource.Success -> {
                    visitedPlaces = visitedPlaces.copy(value = result.data)
                    savedPlaces = result.data ?: listOf()
                }

                is RemoteResource.Error -> {
                    visitedPlaces = visitedPlaces.copy(
                        value = null,
                        error = result.message,
                        isError = true
                    )
                }
            }
            visitedPlaces = visitedPlaces.copy(isLoading = false)
        }
    }

    private fun loadTakenRoutes() {
        viewModelScope.launch {
            takenRoutes = takenRoutes.copy(isLoading = true)
            when (val result = historyRepository.getRoutes()) {
                is RemoteResource.Success -> {
                    takenRoutes = takenRoutes.copy(value = result.data)
                    savedRoutes = result.data ?: listOf()
                }

                is RemoteResource.Error -> {
                    takenRoutes = takenRoutes.copy(
                        value = null,
                        error = result.message,
                        isError = true
                    )
                    savedRoutes = listOf()
                }
            }
            takenRoutes = takenRoutes.copy(isLoading = false)
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
        savedRoutes.forEach {
            if (word.isBlank() || it.name.contains(word, ignoreCase = true)) {
                filtered.add(it)
            }
        }
        takenRoutes = takenRoutes.copy(value = filtered)
    }

    fun filterPlaces(word: String) {
        val filtered = mutableListOf<Place>()
        savedPlaces.forEach {
            if (word.isBlank() || it.name.contains(word, ignoreCase = true)) {
                filtered.add(it)
            }
        }
        visitedPlaces = visitedPlaces.copy(value = filtered)
    }
}