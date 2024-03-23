package ru.nn.tripnn.data.remote.auth

import kotlinx.coroutines.delay
import ru.nn.tripnn.domain.model.LoginData
import ru.nn.tripnn.domain.model.RegistrationData
import ru.nn.tripnn.data.RemoteResource

class FakeAuthenticationServiceImpl : AuthenticationService {
    override suspend fun login(credentials: LoginData): RemoteResource<String> {
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