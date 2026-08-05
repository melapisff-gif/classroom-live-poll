package com.classpoll.teacher.domain.repository

import com.classpoll.teacher.domain.model.User
import com.classpoll.teacher.utils.Resource

interface AuthRepository {
    suspend fun register(name: String, email: String, password: String): Resource<User>
    suspend fun login(email: String, password: String): Resource<User>
    suspend fun logout(): Resource<Unit>
    suspend fun isLoggedIn(): Boolean
    suspend fun getCurrentUser(): User?
}
