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
import ru.nn.tripnn.domain.util.RemoteResource
import ru.nn.tripnn.ui.screen.ResourceState
import javax.inject.Inject

@HiltViewModel
class RecommendationsViewModel @Inject constructor(
    @Fake private val placeRepository: PlaceRepository,
    @Fake private val routeRepository: RouteRepository
) : ViewModel() {
    var recommendedRoutes by mutableStateOf(ResourceState<List<Route>>())
        private set
    val isEmpty
        get() = savedRoutes.isEmpty()
    private var savedRoutes = immutableListOf<Route>()


    fun init() {
        loadRecommended()
    }

    private fun loadRecommended() {
        viewModelScope.launch {
            recommendedRoutes = recommendedRoutes.copy(isLoading = true)
            when(val resource = routeRepository.getRecommendations()) {
                is RemoteResource.Success -> {
                    recommendedRoutes = recommendedRoutes.copy(
                        value = resource.data,
                        error = null,
                        isError = false
                    )
                    savedRoutes = resource.data ?: listOf()
                }

                is RemoteResource.Error -> {
                    recommendedRoutes = recommendedRoutes.copy(
                        value = null,
                        error = resource.message,
                        isError = true,
                    )

                    savedRoutes = listOf()
                }
            }
            recommendedRoutes = recommendedRoutes.copy(isLoading = false)
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
            if (word.isBlank() || it.name.contains(word, ignoreCase = true)) {
                filtered.add(it)
            }
        }
        recommendedRoutes = recommendedRoutes.copy(value = filtered)
    }
}