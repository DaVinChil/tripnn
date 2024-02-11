package ru.nn.tripnn.domain.repository

interface TokenRepository {
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
    suspend fun deleteToken()
}