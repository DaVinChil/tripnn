package ru.nn.tripnn.ui.screen.main.takingroute

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.repository.currentroute.CurrentRouteRepository
import ru.nn.tripnn.data.repository.favourite.FavouritesRepository
import ru.nn.tripnn.data.repository.history.HistoryRepository
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.state.ResFlow
import javax.inject.Inject

@HiltViewModel
class TakingRouteViewModel @Inject constructor(
    private val currentRouteRepository: CurrentRouteRepository,
    private val favouritesRepository: FavouritesRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {
    private val _currentRoute =
        ResFlow(scope = viewModelScope, supplier = currentRouteRepository::getCurrentRoute)
    val currentRoute @Composable get() = _currentRoute.state

    fun addCurrentRouteToFavourite() {
        viewModelScope.launch {
            currentRouteRepository.addCurrentRouteToFavourite()
        }
    }

    fun removeCurrentRouteFromFavourite() {
        viewModelScope.launch {
            currentRouteRepository.removeCurrentRouteFromFavourite()
        }
    }

    fun addPlaceToFavourite(place: Place) {
        viewModelScope.launch {
            favouritesRepository.addPlaceToFavourite(place)
        }
    }

    fun removePlaceFromFavourite(place: Place) {
        viewModelScope.launch {
            favouritesRepository.removePlaceFromFavourite(place)
        }
    }

    fun finishCurrentRoute() {
        viewModelScope.launch {
            _currentRoute.current.getOrNull()?.let {
                val curPlace = it.places.last()
                historyRepository.addPlaceToHistory(curPlace)
                historyRepository.addRouteToHistory(it)
                currentRouteRepository.finishCurrentRoute()
            }
        }
    }

    fun nextPlace() {
        viewModelScope.launch {
            val routeValue = _currentRoute.current.getOrNull()
            val curPlace = routeValue?.places?.get(routeValue.currentPlaceIndex)
            val result = currentRouteRepository.goToNextPlace()
            if (result.isSuccess && curPlace != null) {
                historyRepository.addPlaceToHistory(curPlace)
            }
        }
    }

    fun removePlaceFromRoute(index: Int) {
        viewModelScope.launch {
            currentRouteRepository.removePlaceFromRoute(index)
        }
    }

    fun rateCurrentRoute(rating: Int) {
        viewModelScope.launch {
            currentRouteRepository.rateCurrentRoute(rating)
        }
    }
}