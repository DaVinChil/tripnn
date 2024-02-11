package ru.nn.tripnn.ui.screen

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.screen.HomeScreenData
import ru.nn.tripnn.domain.entity.UserInfo
import ru.nn.tripnn.domain.repository.ScreenDataRepository
import ru.nn.tripnn.domain.repository.UserRepository
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @Fake private val screenDateRepository: ScreenDataRepository,
    @Fake private val userRepository: UserRepository
) : ViewModel() {
    var userState by mutableStateOf(UserState())
        private set

    var homeScreenState by mutableStateOf(HomeScreenState())
        private set

    init {
        loadHomeScreenState()
        loadUserState()
    }

    fun loadHomeScreenState() {
        viewModelScope.launch {
            homeScreenState = homeScreenState.copy(
                isLoading = true,
                error = null
            )

            when(val result = screenDateRepository.getHomeScreenData()) {
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

    fun loadUserState() {
        viewModelScope.launch {
            userState = userState.copy(
                isLoading = true,
                error = null
            )

            when(val result = userRepository.getUserInfo()) {
                is Resource.Success -> {
                    userState = userState.copy(
                        isLoading = false,
                        error = null,
                        userInfo = result.data
                    )
                }
                is Resource.Error -> {
                    userState = userState.copy(
                        isLoading = false,
                        error = result.message,
                        userInfo = null
                    )
                }
            }
        }
    }

    fun changeUserName(name: String) {

    }

    fun clearHistory() {

    }

    fun deleteAccount() {

    }

    fun avatarChange(uri: Uri) {

    }
}

data class UserState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userInfo: UserInfo? = null
)

data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val homeScreenData: HomeScreenData? = null
)