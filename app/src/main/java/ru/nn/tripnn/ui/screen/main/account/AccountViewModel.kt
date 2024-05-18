package ru.nn.tripnn.ui.screen.main.account

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nn.tripnn.data.repository.userinfo.UserInfoRepository
import ru.nn.tripnn.di.Fake
import ru.nn.tripnn.domain.UserInfoData
import ru.nn.tripnn.domain.state.ResFlow
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    @Fake private val userInfoRepository: UserInfoRepository
) : ViewModel() {
    private val _userInfoData = ResFlow(scope = viewModelScope, supplier = ::requestUserInfo)

    val userInfoData @Composable get() = _userInfoData.state

    private fun requestUserInfo(): Flow<Result<UserInfoData>> {
        return flow {
            emit(userInfoRepository.getUserInfo())
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