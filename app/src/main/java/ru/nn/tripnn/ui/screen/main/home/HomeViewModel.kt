package ru.nn.tripnn.ui.screen.main.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.repository.PlaceRepository
import ru.nn.tripnn.domain.repository.RouteRepository
import ru.nn.tripnn.domain.repository.ScreenDataRepository
import ru.nn.tripnn.domain.screen.HomeScreenData
import ru.nn.tripnn.domain.util.RemoteResource
import ru.nn.tripnn.ui.screen.ResourceState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Fake private val screenDataRepository: ScreenDataRepository,
    @Fake private val placeRepository: PlaceRepository,
    @Fake private val routeRepository: RouteRepository
) : ViewModel() {
    var homeScreenState by mutableStateOf(ResourceState<HomeScreenData>())

    fun init() {
        loadHomeScreenState()
    }

    private fun loadHomeScreenState() {
        viewModelScope.launch {
            homeScreenState = homeScreenState.copy(
                isLoading = true,
                isError = false,
                error = null
            )

            when(val result = screenDataRepository.getHomeScreenData()) {
                is RemoteResource.Success -> {
                    homeScreenState = homeScreenState.copy(
                        isLoading = false,
                        isError = false,
                        value = result.data,
                        error = null
                    )
                }
                is RemoteResource.Error -> {
                    homeScreenState = homeScreenState.copy(
                        isLoading = false,
                        isError = true,
                        error = result.message,
                        value = null
                    )
                }
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
}
