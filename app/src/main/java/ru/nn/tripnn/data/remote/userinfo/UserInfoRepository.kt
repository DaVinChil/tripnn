package ru.nn.tripnn.data.remote.userinfo

import ru.nn.tripnn.domain.model.UserInfo
import ru.nn.tripnn.data.RemoteResource

interface UserInfoRepository {
    suspend fun getUserInfo(): RemoteResource<UserInfo>
}