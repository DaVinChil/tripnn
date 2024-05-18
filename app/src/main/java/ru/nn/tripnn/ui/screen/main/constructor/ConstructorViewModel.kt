package ru.nn.tripnn.ui.screen.main.constructor

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.repository.currentroute.CurrentRouteRepository
import ru.nn.tripnn.data.repository.favourite.FavouritesRepository
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.state.ResFlow
import javax.inject.Inject

@HiltViewModel
class ConstructorViewModel @Inject constructor(
    private val currentRouteRepository: CurrentRouteRepository,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    private val _currentRouteState = ResFlow(scope = viewModelScope) {
        currentRouteRepository.getCurrentRoute(true)
    }

    val currentRouteState @Composable get() = _currentRouteState.state

    fun clearCurrentRoute() {
        viewModelScope.launch {
            currentRouteRepository.clearCurrentRoute()
        }
    }

    fun removePlace(index: Int) {
        viewModelScope.launch {
            currentRouteRepository.removePlaceFromRoute(index)
            _currentRouteState.refresh()
        }
    }

    fun addPlace(place: Place) {
        viewModelScope.launch {
            currentRouteRepository.addPlaceToRoute(place.id)
            _currentRouteState.refresh()
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

    fun finishConstructing() {
        viewModelScope.launch {
            currentRouteRepository.takeCurrentRoute()
        }
    }
}