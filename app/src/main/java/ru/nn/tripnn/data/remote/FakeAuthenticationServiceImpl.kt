package ru.nn.tripnn.data.remote

import kotlinx.coroutines.delay
import ru.nn.tripnn.domain.entity.Credentials
import ru.nn.tripnn.domain.repository.AuthenticationService
import ru.nn.tripnn.domain.util.Resource
import javax.inject.Inject

class FakeAuthenticationServiceImpl @Inject constructor(

) : AuthenticationService {
    override suspend fun authenticate(credentials: Credentials): Resource<String> {
        delay(1000)
        return Resource.Success("Kd2Ssdjf832-kLsDs^k92$2")
    }

    override suspend fun authenticate(token: String): Resource<String> {
        delay(1000)
        return Resource.Success("Kd2Ssdjf832-kLsDs^k92$2")
    }

    override suspend fun register(credentials: Credentials): Resource<String> {
        delay(1000)
        return Resource.Success("Kd2Ssdjf832-kLsDs^k92$2")
    }

    override suspend fun logout(token: String) {
        delay(1000)
    }
}