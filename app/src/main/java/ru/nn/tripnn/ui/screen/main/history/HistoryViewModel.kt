package ru.nn.tripnn.ui.screen.main.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.repository.currentroute.CurrentRouteRepository
import ru.nn.tripnn.data.repository.favourite.FavouritesRepository
import ru.nn.tripnn.data.repository.history.HistoryRepository
import ru.nn.tripnn.data.toResultFlow
import ru.nn.tripnn.domain.Place
import ru.nn.tripnn.domain.Route
import ru.nn.tripnn.ui.util.toResourceStateFlow
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val favouritesRepository: FavouritesRepository,
    private val currentRouteRepository: CurrentRouteRepository
) : ViewModel() {
    private val wordFilter = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val visitedPlaces = wordFilter
        .flatMapLatest { historyRepository.getVisitedPlacesByWord(it) }
        .toResourceStateFlow(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val takenRoutes = wordFilter
        .flatMapLatest { historyRepository.getTakenRoutesByWord(it) }
        .toResourceStateFlow(viewModelScope)

    val hasCurrentRoute = currentRouteRepository.getCurrentRoute().map { it.getOrNull() != null }
        .toResultFlow()
        .toResourceStateFlow(viewModelScope)

    fun clearRoutesHistory() {
        viewModelScope.launch {
            historyRepository.clearRoutesHistory()
        }
    }

    fun clearPlacesHistory() {
        viewModelScope.launch {
            historyRepository.clearPlacesHistory()
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