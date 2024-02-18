package ru.nn.tripnn.ui.screen.main.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import ru.nn.tripnn.data.stub_data.PLACE_FULL_1
import ru.nn.tripnn.data.stub_data.ROUTE_FULL
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.entity.Place
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.repository.HistoryRepository
import ru.nn.tripnn.domain.repository.PlaceRepository
import ru.nn.tripnn.domain.repository.RouteRepository
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    @Fake private val historyRepository: HistoryRepository,
    @Fake private val placeRepository: PlaceRepository,
    @Fake private val routeRepository: RouteRepository
) : ViewModel() {
    val isLoading
        get() = isPlacesLoading || isRoutesLoading

    var visitedPlaces by mutableStateOf(immutableListOf<Place>())
    var placeFull by mutableStateOf(PLACE_FULL_1)
        private set

    private var savedPlaces = immutableListOf<Place>()
    private var isPlacesLoading by mutableStateOf(false)

    var takenRoutes by mutableStateOf(immutableListOf<Route>())
    var routeFull by mutableStateOf(ROUTE_FULL)
        private set

    private var savedRoutes = immutableListOf<Route>()
    private var isRoutesLoading by mutableStateOf(false)

    init {
        loadVisitedPlaces()
        loadTakenRoutes()
    }

    private fun loadVisitedPlaces() {
        viewModelScope.launch {
            isPlacesLoading = true
            when (val result = historyRepository.getPlaces()) {
                is Resource.Success -> {
                    visitedPlaces = result.data ?: listOf()
                    savedPlaces = visitedPlaces
                }

                is Resource.Error -> {

                }
            }
            isPlacesLoading = false
        }
    }

    private fun loadTakenRoutes() {
        viewModelScope.launch {
            isRoutesLoading = true
            when (val result = historyRepository.getRoutes()) {
                is Resource.Success -> {
                    takenRoutes = result.data ?: listOf()
                    savedRoutes = takenRoutes
                }

                is Resource.Error -> {

                }
            }
            isRoutesLoading = false
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
        takenRoutes = filtered
    }

    fun filterPlaces(word: String) {
        val filtered = mutableListOf<Place>()
        savedPlaces.forEach {
            if (word.isNullOrBlank() || it.name.contains(word, ignoreCase = true)) {
                filtered.add(it)
            }
        }
        visitedPlaces = filtered
    }

    fun getFullPlaceInfo(id: String) {
        viewModelScope.launch {
            when (val result = placeRepository.getFullInfo(id)) {
                is Resource.Success -> {
                    placeFull = result.data ?: placeFull
                }

                is Resource.Error -> {

                }
            }
        }
    }

    fun getFullRouteInfo(id: String) {
        viewModelScope.launch {
            when (val result = routeRepository.getFullInfo(id)) {
                is Resource.Success -> {
                    routeFull = result.data ?: routeFull
                }

                is Resource.Error -> {

                }
            }
        }
    }
}