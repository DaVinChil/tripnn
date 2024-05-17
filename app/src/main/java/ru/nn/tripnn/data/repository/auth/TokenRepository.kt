package ru.nn.tripnn.data.repository.auth

import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    fun getToken(): Flow<Result<String?>>
    suspend fun saveToken(token: String): Result<Unit>
    suspend fun deleteToken(): Result<Unit>
}