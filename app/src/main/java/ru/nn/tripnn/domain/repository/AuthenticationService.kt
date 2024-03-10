package ru.nn.tripnn.domain.repository

import ru.nn.tripnn.domain.entity.LogInData
import ru.nn.tripnn.domain.entity.RegistrationData
import ru.nn.tripnn.domain.util.RemoteResource

interface AuthenticationService {
    suspend fun login(credentials: LogInData): RemoteResource<String>
    suspend fun authenticate(token: String): RemoteResource<String>
    suspend fun register(credentials: RegistrationData): RemoteResource<String>
    suspend fun logout(token: String)
}