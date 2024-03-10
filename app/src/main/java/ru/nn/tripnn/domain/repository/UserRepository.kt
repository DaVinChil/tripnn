package ru.nn.tripnn.domain.repository

import ru.nn.tripnn.domain.entity.UserInfo
import ru.nn.tripnn.domain.util.RemoteResource

interface UserRepository {
    suspend fun getUserInfo(): RemoteResource<UserInfo>
}