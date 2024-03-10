package ru.nn.tripnn.data.remote.repository

import kotlinx.coroutines.delay
import ru.nn.tripnn.data.stub_data.userInfo
import ru.nn.tripnn.domain.entity.UserInfo
import ru.nn.tripnn.domain.repository.UserRepository
import ru.nn.tripnn.domain.util.RemoteResource
import javax.inject.Inject

class FakeUserRepositoryImpl @Inject constructor() : UserRepository {
    override suspend fun getUserInfo(): RemoteResource<UserInfo> {
        delay(3000)
        return RemoteResource.Success(userInfo)
    }
}