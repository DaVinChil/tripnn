package ru.nn.tripnn.ui.screen.main.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.remote.currentroute.CurrentRouteRepository
import ru.nn.tripnn.data.remote.place.PlaceRepository
import ru.nn.tripnn.data.remote.route.RouteRepository
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.model.CurrentRoute
import ru.nn.tripnn.domain.model.HomeScreenData
import ru.nn.tripnn.domain.model.Route
import ru.nn.tripnn.ui.screen.authentication.ResourceState
import ru.nn.tripnn.ui.util.resourceStateFromRequest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Fake private val placeRepository: PlaceRepository,
    @Fake private val routeRepository: RouteRepository,
    @Fake private val currentRouteRepository: CurrentRouteRepository
) : ViewModel() {
    var homeScreenState by mutableStateOf(ResourceState<HomeScreenData>())
        private set
    var currentRoute by mutableStateOf(ResourceState<CurrentRoute>())
        private set
    var recommendedRoutes by mutableStateOf(ResourceState<List<Route>>())
        private set

    init {
        loadCurrentRoute()
        loadRecommendedRoutes()
    }

    private fun loadCurrentRoute() {
        viewModelScope.launch {
            resourceStateFromRequest { currentRouteRepository.getCurrentRoute() }.collectLatest {
                currentRoute = it
            }
        }
    }

    private fun loadRecommendedRoutes() {
        viewModelScope.launch {
            resourceStateFromRequest { routeRepository.getRecommendations() }.collectLatest {
                recommendedRoutes = it
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
}
