package ru.nn.tripnn.ui.screen.main.account

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.model.UserInfo
import ru.nn.tripnn.data.remote.userinfo.UserInfoRepository
import ru.nn.tripnn.data.RemoteResource
import ru.nn.tripnn.ui.screen.authentication.ResourceState
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    @Fake private val userInfoRepository: UserInfoRepository
) : ViewModel() {
    var userInfo by mutableStateOf(ResourceState<UserInfo>())
        private set

    fun init() {
        loadUserState()
    }

    private fun loadUserState() {
        viewModelScope.launch {
            userInfo = userInfo.copy(
                isLoading = true,
                error = null
            )

            when(val result = userInfoRepository.getUserInfo()) {
                is RemoteResource.Success -> {
                    userInfo = userInfo.copy(
                        isLoading = false,
                        error = null,
                        value = result.data
                    )
                }
                is RemoteResource.Error -> {
                    userInfo = userInfo.copy(
                        isLoading = false,
                        error = result.message,
                        value = null
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