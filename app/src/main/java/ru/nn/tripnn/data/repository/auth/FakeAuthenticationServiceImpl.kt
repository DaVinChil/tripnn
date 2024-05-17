package ru.nn.tripnn.data.repository.auth

import kotlinx.coroutines.delay
import ru.nn.tripnn.domain.LoginData
import ru.nn.tripnn.domain.RegistrationData

class FakeAuthenticationServiceImpl : AuthenticationService {
    override suspend fun login(credentials: LoginData): Result<String> {
        delay(2000)
        return Result.success("Kd2Ssdjf832-kLsDs^k92$2")
    }

    override suspend fun authenticate(token: String): Result<String> {
        delay(2000)
        return Result.success("Kd2Ssdjf832-kLsDs^k92$2")
    }

    override suspend fun register(credentials: RegistrationData): Result<String> {
        delay(2000)
        return Result.success("Kd2Ssdjf832-kLsDs^k92$2")
    }

    override suspend fun logout(token: String): Result<Unit> {
        delay(2000)
        return Result.success(Unit)
    }
}