package ru.nn.tripnn.data.remote.auth

import ru.nn.tripnn.domain.LoginData
import ru.nn.tripnn.domain.RegistrationData
import ru.nn.tripnn.data.RemoteResource

interface AuthenticationService {
    suspend fun login(credentials: LoginData): RemoteResource<String>
    suspend fun authenticate(token: String): RemoteResource<String>
    suspend fun register(credentials: RegistrationData): RemoteResource<String>
    suspend fun logout(token: String)
}