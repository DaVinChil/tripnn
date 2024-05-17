package ru.nn.tripnn.data.repository.userinfo

import ru.nn.tripnn.domain.UserInfoData

interface UserInfoRepository {
    suspend fun getUserInfo(): Result<UserInfoData>
}