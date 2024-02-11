package ru.nn.tripnn.data.repository

import ru.nn.tripnn.data.stub_data.userInfo
import ru.nn.tripnn.domain.repository.UserRepository
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject

class FakeUserRepositoryImpl @Inject constructor() : UserRepository {
    override suspend fun getUserInfo() = Resource.Success(userInfo)
}