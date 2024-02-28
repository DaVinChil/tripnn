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
import ru.nn.tripnn.domain.util.Resource
import ru.nn.tripnn.ui.screen.RemoteResource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Fake private val screenDataRepository: ScreenDataRepository,
    @Fake private val placeRepository: PlaceRepository,
    @Fake private val routeRepository: RouteRepository
) : ViewModel() {
    var homeScreenState by mutableStateOf(RemoteResource<HomeScreenData>())

    init {
        loadHomeScreenState()
    }

    fun loadHomeScreenState() {
        viewModelScope.launch {
            homeScreenState = homeScreenState.copy(
                isLoading = true,
                isError = false,
                message = null
            )

            when(val result = screenDataRepository.getHomeScreenData()) {
                is Resource.Success -> {
                    homeScreenState = homeScreenState.copy(
                        isLoading = false,
                        isError = false,
                        value = result.data
                    )
                }
                is Resource.Error -> {
                    homeScreenState = homeScreenState.copy(
                        isLoading = false,
                        isError = true,
                        message = result.message,
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
