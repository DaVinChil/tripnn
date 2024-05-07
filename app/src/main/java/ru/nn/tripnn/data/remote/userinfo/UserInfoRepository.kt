package ru.nn.tripnn.data.remote.userinfo

import ru.nn.tripnn.domain.UserInfoData
import ru.nn.tripnn.data.RemoteResource

interface UserInfoRepository {
    suspend fun getUserInfo(): RemoteResource<UserInfoData>
}