package com.classpoll.student.domain.repository

import com.classpoll.student.domain.model.User
import com.classpoll.student.utils.Resource

interface AuthRepository {
    suspend fun register(name: String, email: String, password: String): Resource<User>
    suspend fun login(email: String, password: String): Resource<User>
    suspend fun logout(): Resource<Unit>
    suspend fun isLoggedIn(): Boolean
    suspend fun getCurrentUser(): User?
}
