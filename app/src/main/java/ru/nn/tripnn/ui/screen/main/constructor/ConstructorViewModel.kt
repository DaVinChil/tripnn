package ru.nn.tripnn.ui.screen.main.constructor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.local.currentroute.CurrentRouteRepository
import ru.nn.tripnn.data.remote.place.PlaceRepository
import ru.nn.tripnn.data.remote.routebuilder.RouteBuilderService
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.CurrentRoute
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.ui.screen.ResourceState
import ru.nn.tripnn.ui.util.resourceStateFromRequest
import javax.inject.Inject

@HiltViewModel
class ConstructorViewModel @Inject constructor(
    @Fake private val routeBuilderService: RouteBuilderService,
    private val currentRouteRepository: CurrentRouteRepository,
    @Fake private val placeRepository: PlaceRepository
) : ViewModel() {

    var currentRouteState by mutableStateOf(ResourceState(CurrentRoute()))
        private set

    fun init() {
        loadCurrentRoute()
    }

    private fun loadCurrentRoute() {
        viewModelScope.launch {
            resourceStateFromRequest(currentRouteRepository::getCurrentRoute).collectLatest {
                currentRouteState = it
            }
        }
    }

    fun removePlace(index: Int) {
        viewModelScope.launch {
            var currentRoute = if (currentRouteState.value != null) {
                currentRouteState.value!!
            } else {
                CurrentRoute()
            }

            val currentPlaces = currentRoute.places.toMutableList()

            currentPlaces.removeAt(index)

            if (index > 0 && index < currentPlaces.lastIndex) {
                val newTimeToWalk = routeBuilderService.timeToWalk(
                    currentPlaces[index - 1],
                    currentPlaces[index]
                )
                currentPlaces[index] = currentPlaces[index].copy(timeToGo = newTimeToWalk)
            }

            currentRoute = currentRoute.copy(places = currentPlaces)
            currentRouteState = currentRouteState.copy(value = currentRoute)

            currentRouteRepository.saveCurrentRoute(currentRoute)
        }
    }

    fun addPlace(place: Place) {
        viewModelScope.launch {
            var currentRoute = if (currentRouteState.value != null) {
                currentRouteState.value!!
            } else {
                CurrentRoute()
            }

            val currentPlaces = currentRoute.places.toMutableList()

            currentPlaces.add(place)

            currentRoute = currentRoute.copy(places = currentPlaces)
            currentRouteState = currentRouteState.copy(value = currentRoute)
            currentRouteRepository.saveCurrentRoute(currentRoute)
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

    fun finishConstructing() {
        viewModelScope.launch {
            var currentRoute = currentRouteState.value!!
            currentRoute = currentRoute.copy(
                buildInProgress = false
            )
            currentRouteRepository.saveCurrentRoute(currentRoute)
        }
    }
}