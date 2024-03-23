package ru.nn.tripnn.ui.screen.main.history

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
import ru.nn.tripnn.data.remote.history.HistoryRepository
import ru.nn.tripnn.data.remote.place.PlaceRepository
import ru.nn.tripnn.data.remote.route.RouteRepository
import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.ui.screen.authentication.ResourceState
import ru.nn.tripnn.ui.util.resourceStateFromRequest
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    @Fake private val historyRepository: HistoryRepository,
    @Fake private val placeRepository: PlaceRepository,
    @Fake private val routeRepository: RouteRepository
) : ViewModel() {

    var visitedPlaces by mutableStateOf(ResourceState<List<Place>>())
    private var savedVisitedPlaces = immutableListOf<Place>()

    var takenRoutes by mutableStateOf(ResourceState<List<Route>>())
    private var savedTakenRoutes = immutableListOf<Route>()

    fun init() {
        loadVisitedPlaces()
        loadTakenRoutes()
    }

    private fun loadVisitedPlaces() {
        viewModelScope.launch {
            if (savedVisitedPlaces.isNotEmpty()) {
                visitedPlaces = visitedPlaces.copy(value = savedVisitedPlaces)
                return@launch
            }

            resourceStateFromRequest(historyRepository::getPlaces).collectLatest {
                visitedPlaces = it
                savedVisitedPlaces = it.value ?: listOf()
            }
        }
    }

    private fun loadTakenRoutes() {
        viewModelScope.launch {
            if (savedTakenRoutes.isNotEmpty()) {
                takenRoutes = takenRoutes.copy(value = savedTakenRoutes)
                return@launch
            }

            resourceStateFromRequest(historyRepository::getRoutes).collectLatest {
                takenRoutes = it
                savedTakenRoutes = it.value ?: listOf()
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
        val filtered = filterByWord(word, savedTakenRoutes, Route::name)
        takenRoutes = takenRoutes.copy(value = filtered)
    }

    fun filterPlaces(word: String) {
        val filtered = filterByWord(word, savedVisitedPlaces, Place::name)
        visitedPlaces = visitedPlaces.copy(value = filtered)
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