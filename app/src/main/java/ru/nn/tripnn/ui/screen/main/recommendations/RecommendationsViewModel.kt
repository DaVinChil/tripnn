package ru.nn.tripnn.ui.screen.main.recommendations

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.repository.currentroute.CurrentRouteRepository
import ru.nn.tripnn.data.repository.favourite.FavouritesRepository
import ru.nn.tripnn.data.repository.route.RouteRecommendationsRepository
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.domain.state.ResFlow
import javax.inject.Inject

@HiltViewModel
class RecommendationsViewModel @Inject constructor(
    private val favouritesRepository: FavouritesRepository,
    private val routeRecommendationsRepository: RouteRecommendationsRepository,
    private val currentRouteRepository: CurrentRouteRepository
) : ViewModel() {
    private val wordFilter = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _recommendedRoutes = ResFlow(scope = viewModelScope) {
        wordFilter.flatMapLatest(routeRecommendationsRepository::getRecommendationsByWord)
    }

    private val _hasCurrentRoute = ResFlow(
        scope = viewModelScope,
        supplier = currentRouteRepository::currentRouteExists
    )

    val recommendedRoutes @Composable get() = _recommendedRoutes.state
    val hasCurrentRoute @Composable get() = _hasCurrentRoute.state

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

    fun filterByWord(word: String) {
        viewModelScope.launch {
            wordFilter.emit(word)
        }
    }

    fun setCurrentRoute(route: Route) {
        viewModelScope.launch {
            currentRouteRepository.takeTheRoute(route)
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            wordFilter.emit("")
        }
    }
}