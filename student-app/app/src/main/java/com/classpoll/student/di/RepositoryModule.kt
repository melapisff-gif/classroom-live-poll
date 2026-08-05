package com.classpoll.student.di

import com.classpoll.student.data.repository.AuthRepositoryImpl
import com.classpoll.student.data.repository.ClassroomRepositoryImpl
import com.classpoll.student.data.repository.PollRepositoryImpl
import com.classpoll.student.domain.repository.AuthRepository
import com.classpoll.student.domain.repository.ClassroomRepository
import com.classpoll.student.domain.repository.PollRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindClassroomRepository(impl: ClassroomRepositoryImpl): ClassroomRepository

    @Binds
    @Singleton
    abstract fun bindPollRepository(impl: PollRepositoryImpl): PollRepository
}
