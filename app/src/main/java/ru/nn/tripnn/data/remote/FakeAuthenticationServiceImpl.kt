package ru.nn.tripnn.data.remote

import kotlinx.coroutines.delay
import ru.nn.tripnn.domain.entity.LogInData
import ru.nn.tripnn.domain.entity.RegistrationData
import ru.nn.tripnn.domain.repository.AuthenticationService
import ru.nn.tripnn.domain.util.RemoteResource

class FakeAuthenticationServiceImpl : AuthenticationService {
    override suspend fun login(credentials: LogInData): RemoteResource<String> {
        delay(2000)
        return RemoteResource.Success("Kd2Ssdjf832-kLsDs^k92$2")
    }

    override suspend fun authenticate(token: String): RemoteResource<String> {
        delay(2000)
        return RemoteResource.Success("Kd2Ssdjf832-kLsDs^k92$2")
    }

    override suspend fun register(credentials: RegistrationData): RemoteResource<String> {
        delay(2000)
        return RemoteResource.Success("Kd2Ssdjf832-kLsDs^k92$2")
    }

    override suspend fun logout(token: String) {
        delay(2000)
    }
}