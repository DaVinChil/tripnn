package ru.nn.tripnn.data.local.token

interface TokenRepository {
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
    suspend fun deleteToken()
}