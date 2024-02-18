package ru.nn.tripnn.ui.screen.main.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.stub_data.PLACE_FULL_1
import ru.nn.tripnn.data.stub_data.ROUTE_FULL
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.repository.PlaceRepository
import ru.nn.tripnn.domain.repository.RouteRepository
import ru.nn.tripnn.domain.repository.ScreenDataRepository
import ru.nn.tripnn.domain.screen.HomeScreenData
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject public constructor(
    @Fake private val screenDataRepository: ScreenDataRepository,
    @Fake private val placeRepository: PlaceRepository,
    @Fake private val routeRepository: RouteRepository
) : ViewModel() {
    var homeScreenState by mutableStateOf(HomeScreenState())
        private set
    var placeFull by mutableStateOf(PLACE_FULL_1)
        private set

    var routeFull by mutableStateOf(ROUTE_FULL)
        private set

    init {
        loadHomeScreenState()
    }

    fun loadHomeScreenState() {
        viewModelScope.launch {
            homeScreenState = homeScreenState.copy(
                isLoading = true,
                error = null
            )

            when(val result = screenDataRepository.getHomeScreenData()) {
                is Resource.Success -> {
                    homeScreenState = homeScreenState.copy(
                        isLoading = false,
                        error = null,
                        homeScreenData = result.data
                    )
                }
                is Resource.Error -> {
                    homeScreenState = homeScreenState.copy(
                        isLoading = false,
                        error = result.message,
                        homeScreenData = null
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

data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val homeScreenData: HomeScreenData? = null
)