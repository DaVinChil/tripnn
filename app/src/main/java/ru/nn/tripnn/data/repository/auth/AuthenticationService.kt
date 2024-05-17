package ru.nn.tripnn.data.repository.auth

import ru.nn.tripnn.domain.LoginData
import ru.nn.tripnn.domain.RegistrationData

interface AuthenticationService {
    suspend fun login(credentials: LoginData): Result<String>
    suspend fun authenticate(token: String): Result<String>
    suspend fun register(credentials: RegistrationData): Result<String>
    suspend fun logout(token: String): Result<Unit>
}