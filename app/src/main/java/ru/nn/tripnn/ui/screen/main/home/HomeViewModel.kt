package ru.nn.tripnn.ui.screen.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.repository.currentroute.CurrentRouteRepository
import ru.nn.tripnn.data.repository.favourite.FavouritesRepository
import ru.nn.tripnn.data.repository.route.RouteRecommendationsRepository
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.ui.util.toResourceStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val favouritesRepository: FavouritesRepository,
    private val routeRecommendationsRepository: RouteRecommendationsRepository,
    private val currentRouteRepository: CurrentRouteRepository
) : ViewModel() {
    val currentRoute = currentRouteRepository.getCurrentRoute()
        .toResourceStateFlow(viewModelScope)

    val recommendedRoutes = routeRecommendationsRepository.getRecommendations()
        .toResourceStateFlow(viewModelScope)

    fun isRouteInProgress(): Boolean {
        return !currentRoute.value.isLoading &&
                !currentRoute.value.isError &&
                currentRoute.value.state != null &&
                currentRoute.value.state?.buildInProgress == false
    }

    fun deleteCurrentRoute() {
        viewModelScope.launch {
            currentRouteRepository.deleteCurrentRoute()
        }
    }

    fun removePlaceFromFavourite(place: Place) {
        viewModelScope.launch {
            favouritesRepository.removePlaceFromFavourite(place)
        }
    }

    fun addPlaceToFavourite(place: Place) {
        viewModelScope.launch {
            favouritesRepository.addPlaceToFavourite(place)
        }
    }

    fun removeRouteFromFavourite(route: Route) {
        viewModelScope.launch {
            favouritesRepository.removeRouteFromFavourite(route)
        }
    }

    fun addRouteToFavourite(route: Route) {
        viewModelScope.launch {
            favouritesRepository.addRouteToFavourite(route)
        }
    }

    fun setCurrentRoute(route: Route) {
        viewModelScope.launch {
            currentRouteRepository.takeTheRoute(route)
        }
    }
}
