package ru.nn.tripnn.ui.screen.main.account

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nn.tripnn.data.repository.userinfo.UserInfoRepository
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.UserInfoData
import ru.nn.tripnn.ui.screen.ResourceState
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    @Fake private val userInfoRepository: UserInfoRepository
) : ViewModel() {
    var userInfoData by mutableStateOf(ResourceState<UserInfoData>())
        private set

    fun init() {
        loadUserState()
    }

    private fun loadUserState() {
        viewModelScope.launch {
            userInfoData = userInfoData.copy(
                isLoading = true,
                error = null
            )

            val result = userInfoRepository.getUserInfo()
            when {
                result.isSuccess -> userInfoData = userInfoData.toSuccess(result.getOrNull())
                result.isFailure -> userInfoData = userInfoData.toError(result.exceptionOrNull())
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