package ru.nn.tripnn.domain.repository

import ru.nn.tripnn.domain.entity.UserInfo
import ru.nn.tripnn.domain.util.Resource

interface UserRepository {
    suspend fun getUserInfo(): Resource<UserInfo>
}