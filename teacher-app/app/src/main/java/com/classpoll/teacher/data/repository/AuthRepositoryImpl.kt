package com.classpoll.teacher.data.repository

import com.classpoll.teacher.data.local.TokenManager
import com.classpoll.teacher.data.remote.api.AuthApi
import com.classpoll.teacher.data.remote.dto.*
import com.classpoll.teacher.domain.model.User
import com.classpoll.teacher.domain.repository.AuthRepository
import com.classpoll.teacher.utils.Resource
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun register(name: String, email: String, password: String): Resource<User> {
        return try {
            val response = authApi.register(RegisterRequest(name, email, password))
            if (response.isSuccessful) {
                val body = response.body()!!
                tokenManager.saveTokens(body.accessToken, body.refreshToken)
                tokenManager.saveUser(body.teacher.id, body.teacher.name, body.teacher.email)
                Resource.Success(User(body.teacher.id, body.teacher.name, body.teacher.email))
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun login(email: String, password: String): Resource<User> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val body = response.body()!!
                tokenManager.saveTokens(body.accessToken, body.refreshToken)
                tokenManager.saveUser(body.teacher.id, body.teacher.name, body.teacher.email)
                Resource.Success(User(body.teacher.id, body.teacher.name, body.teacher.email))
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun logout(): Resource<Unit> {
        return try {
            val token = tokenManager.refreshToken.first()
            if (token != null) {
                authApi.logout(LogoutRequest(token))
            }
            tokenManager.clearAll()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return tokenManager.accessToken.first() != null
    }

    override suspend fun getCurrentUser(): User? {
        val id = tokenManager.userId.first() ?: return null
        val name = tokenManager.userName.first() ?: return null
        val email = tokenManager.userEmail.first() ?: return null
        return User(id, name, email)
    }
}
