package ru.nn.tripnn.ui.screen.main.recommendations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.entity.Route
import ru.nn.tripnn.domain.repository.PlaceRepository
import ru.nn.tripnn.domain.repository.RouteRepository
import ru.nn.tripnn.domain.util.Resource
import ru.nn.tripnn.ui.screen.main.favourite.ResourceListState
import javax.inject.Inject

@HiltViewModel
class RecommendationsViewModel @Inject constructor(
    @Fake private val placeRepository: PlaceRepository,
    @Fake private val routeRepository: RouteRepository
) : ViewModel() {
    var recommendedRoutes by mutableStateOf(ResourceListState<Route>())
        private set
    private var savedRoutes = immutableListOf<Route>()

    init {
        loadRecommended()
    }

    private fun loadRecommended() {
        viewModelScope.launch {
            recommendedRoutes = recommendedRoutes.copy(isLoading = true)
            when(val resource = routeRepository.getRecommendations()) {
                is Resource.Success -> {
                    recommendedRoutes = recommendedRoutes.copy(list = resource.data ?: listOf())
                }

                is Resource.Error -> {

                }
            }
            recommendedRoutes = recommendedRoutes.copy(isLoading = true)
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
        recommendedRoutes = recommendedRoutes.copy(list = filtered)
    }
}