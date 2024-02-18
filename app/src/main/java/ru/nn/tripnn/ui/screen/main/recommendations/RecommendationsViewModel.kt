package ru.nn.tripnn.ui.screen.main.recommendations

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
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.repository.PlaceRepository
import ru.nn.tripnn.domain.repository.RouteRepository
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class RecommendationsViewModel @Inject constructor(
    @Fake private val placeRepository: PlaceRepository,
    @Fake private val routeRepository: RouteRepository
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var recommendedRoutes by mutableStateOf(immutableListOf<Route>())
        private set
    private var savedRoutes = immutableListOf<Route>()

    var placeFull by mutableStateOf(PLACE_FULL_1)
        private set
    var routeFull by mutableStateOf(ROUTE_FULL)
        private set

    init {
        loadRecommended()
    }

    private fun loadRecommended() {
        viewModelScope.launch {
            isLoading = true
            when(val resource = routeRepository.getRecommendations()) {
                is Resource.Success -> {
                    recommendedRoutes = resource.data ?: recommendedRoutes
                }

                is Resource.Error -> {

                }
            }
            isLoading = false
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
        recommendedRoutes = filtered
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