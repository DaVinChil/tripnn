package ru.nn.tripnn.data.remote.userinfo

import kotlinx.coroutines.delay
import ru.nn.tripnn.data.stub_data.userInfo
import ru.nn.tripnn.domain.model.UserInfo
import ru.nn.tripnn.data.RemoteResource

class FakeUserInfoRepositoryImpl : UserInfoRepository {
    override suspend fun getUserInfo(): RemoteResource<UserInfo> {
        delay(3000)
        return RemoteResource.Success(userInfo)
    }
}