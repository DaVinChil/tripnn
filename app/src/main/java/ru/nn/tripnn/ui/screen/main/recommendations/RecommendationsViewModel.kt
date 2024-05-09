package ru.nn.tripnn.ui.screen.main.recommendations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import ru.nn.tripnn.data.local.currentroute.CurrentRouteRepository
import ru.nn.tripnn.data.remote.place.PlaceRepository
import ru.nn.tripnn.data.remote.route.RouteRepository
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.util.convertRouteToCurrentRoute
import ru.nn.tripnn.ui.util.resourceStateFromRequest
import javax.inject.Inject

@HiltViewModel
class RecommendationsViewModel @Inject constructor(
    @Fake private val placeRepository: PlaceRepository,
    @Fake private val routeRepository: RouteRepository,
    private val currentRouteRepository: CurrentRouteRepository
) : ViewModel() {
    var recommendedRoutes by mutableStateOf(ResourceState<List<Route>>())
        private set
    val isEmpty
        get() = savedRoutes.isEmpty()
    private var savedRoutes = immutableListOf<Route>()

    var hasCurrentRoute by mutableStateOf(false)

    fun init() {
        loadRecommended()
        checkExistsCurrentRoute()
    }

    private fun loadRecommended() {
        viewModelScope.launch {
            if (!isEmpty) {
                recommendedRoutes = recommendedRoutes.copy(value = savedRoutes)
                return@launch
            }

            resourceStateFromRequest(routeRepository::getRecommendations).collectLatest {
                recommendedRoutes = it
                savedRoutes = it.value ?: listOf()
            }
        }
    }

    private fun checkExistsCurrentRoute() {
        viewModelScope.launch {
            resourceStateFromRequest(currentRouteRepository::getCurrentRoute).collectLatest {
                hasCurrentRoute = it.value != null
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

    fun removeRouteFromFavourite(route: Route) {
        viewModelScope.launch {
            if (route.id == null) return@launch
            routeRepository.removeFromFavourite(route.id)
        }
    }

    fun addRouteToFavourite(route: Route) {
        viewModelScope.launch {
            if (route.id == null) return@launch
            routeRepository.addToFavourite(route.id)
        }
    }

    fun setCurrentRoute(route: Route) {
        viewModelScope.launch {
            currentRouteRepository.saveCurrentRoute(convertRouteToCurrentRoute(route))
        }
    }

    fun filterRoutes(word: String) {
        val filtered = mutableListOf<Route>()
        savedRoutes.forEach {
            if (word.isBlank() || it.name.contains(word, ignoreCase = true)) {
                filtered.add(it)
            }
        }
        recommendedRoutes = recommendedRoutes.copy(value = filtered)
    }
}