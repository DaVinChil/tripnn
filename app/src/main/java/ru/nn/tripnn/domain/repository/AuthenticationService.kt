package ru.nn.tripnn.domain.repository

import ru.nn.tripnn.domain.entity.Credentials
import ru.nn.tripnn.domain.util.Resource

interface AuthenticationService {
    suspend fun authenticate(credentials: Credentials): Resource<String>
    suspend fun authenticate(token: String): Resource<String>
    suspend fun register(credentials: Credentials): Resource<String>
    suspend fun logout(token: String)
}