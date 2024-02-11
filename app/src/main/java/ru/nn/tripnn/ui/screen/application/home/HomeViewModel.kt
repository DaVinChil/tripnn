package ru.nn.tripnn.ui.screen.application.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.repository.ScreenDataRepository
import ru.nn.tripnn.domain.screen.HomeScreenData
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Fake private val screenDataRepository: ScreenDataRepository
) : ViewModel() {
    var homeScreenState by mutableStateOf(HomeScreenState())
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
}

data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val homeScreenData: HomeScreenData? = null
)