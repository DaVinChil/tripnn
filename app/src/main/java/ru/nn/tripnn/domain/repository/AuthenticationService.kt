package ru.nn.tripnn.domain.repository

import ru.nn.tripnn.domain.entity.LogInData
import ru.nn.tripnn.domain.entity.RegistrationData
import ru.nn.tripnn.domain.util.Resource

interface AuthenticationService {
    suspend fun login(credentials: LogInData): Resource<String>
    suspend fun authenticate(token: String): Resource<String>
    suspend fun register(credentials: RegistrationData): Resource<String>
    suspend fun logout(token: String)
}