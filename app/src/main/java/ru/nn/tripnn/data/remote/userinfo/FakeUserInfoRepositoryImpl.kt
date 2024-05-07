package ru.nn.tripnn.data.remote.userinfo

import kotlinx.coroutines.delay
import ru.nn.tripnn.data.stub_data.userInfoData
import ru.nn.tripnn.domain.UserInfoData
import ru.nn.tripnn.data.RemoteResource

class FakeUserInfoRepositoryImpl : UserInfoRepository {
    override suspend fun getUserInfo(): RemoteResource<UserInfoData> {
        delay(3000)
        return RemoteResource.Success(userInfoData)
    }
}