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
import ru.nn.tripnn.domain.util.Resource
import ru.nn.tripnn.ui.screen.main.favourite.ResourceListState
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    @Fake private val historyRepository: HistoryRepository,
    @Fake private val placeRepository: PlaceRepository,
    @Fake private val routeRepository: RouteRepository
) : ViewModel() {
    val isLoading
        get() = visitedPlaces.isLoading || takenRoutes.isLoading

    var visitedPlaces by mutableStateOf(ResourceListState<Place>())
    private var savedPlaces = immutableListOf<Place>()

    var takenRoutes by mutableStateOf(ResourceListState<Route>())
    private var savedRoutes = immutableListOf<Route>()

    init {
        loadVisitedPlaces()
        loadTakenRoutes()
    }

    private fun loadVisitedPlaces() {
        viewModelScope.launch {
            visitedPlaces = visitedPlaces.copy(isLoading = true)
            when (val result = historyRepository.getPlaces()) {
                is Resource.Success -> {
                    visitedPlaces = visitedPlaces.copy(list = result.data ?: listOf())
                    savedPlaces = visitedPlaces.list
                }

                is Resource.Error -> {

                }
            }
            visitedPlaces = visitedPlaces.copy(isLoading = false)
        }
    }

    private fun loadTakenRoutes() {
        viewModelScope.launch {
            takenRoutes = takenRoutes.copy(isLoading = true)
            when (val result = historyRepository.getRoutes()) {
                is Resource.Success -> {
                    takenRoutes = takenRoutes.copy(list = result.data ?: listOf())
                    savedRoutes = takenRoutes.list
                }

                is Resource.Error -> {

                }
            }
            takenRoutes = takenRoutes.copy(isLoading = true)
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
            if (word.isNullOrBlank() || it.name.contains(word, ignoreCase = true)) {
                filtered.add(it)
            }
        }
        takenRoutes = takenRoutes.copy(list = filtered)
    }

    fun filterPlaces(word: String) {
        val filtered = mutableListOf<Place>()
        savedPlaces.forEach {
            if (word.isNullOrBlank() || it.name.contains(word, ignoreCase = true)) {
                filtered.add(it)
            }
        }
        visitedPlaces = visitedPlaces.copy(list = filtered)
    }
}