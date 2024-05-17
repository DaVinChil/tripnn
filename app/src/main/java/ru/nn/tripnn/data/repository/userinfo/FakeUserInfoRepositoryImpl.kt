package ru.nn.tripnn.data.repository.userinfo

import kotlinx.coroutines.delay
import ru.nn.tripnn.data.datasource.stubdata.ui.userInfoData
import ru.nn.tripnn.domain.UserInfoData

class FakeUserInfoRepositoryImpl : UserInfoRepository {
    override suspend fun getUserInfo(): Result<UserInfoData> {
        delay(3000)
        return Result.success(userInfoData)
    }
}