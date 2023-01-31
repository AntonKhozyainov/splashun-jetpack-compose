package ru.khozyainov.splashun.data.repository.auth

import android.content.Intent
import kotlinx.coroutines.flow.Flow
import net.openid.appauth.TokenRequest

interface AuthRepository {
    fun getTokenFlow(): Flow<String?>
    fun getLoginPageIntent(): Intent
    suspend fun getTokenByRequest(tokenRequest: TokenRequest): String
}