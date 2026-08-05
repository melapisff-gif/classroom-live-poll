package com.classpoll.teacher.di

import com.classpoll.teacher.data.repository.AnalyticsRepositoryImpl
import com.classpoll.teacher.data.repository.AuthRepositoryImpl
import com.classpoll.teacher.data.repository.ClassroomRepositoryImpl
import com.classpoll.teacher.data.repository.LeaderboardRepositoryImpl
import com.classpoll.teacher.data.repository.PollRepositoryImpl
import com.classpoll.teacher.domain.repository.AnalyticsRepository
import com.classpoll.teacher.domain.repository.AuthRepository
import com.classpoll.teacher.domain.repository.ClassroomRepository
import com.classpoll.teacher.domain.repository.LeaderboardRepository
import com.classpoll.teacher.domain.repository.PollRepository
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

    @Binds
    @Singleton
    abstract fun bindLeaderboardRepository(impl: LeaderboardRepositoryImpl): LeaderboardRepository

    @Binds
    @Singleton
    abstract fun bindAnalyticsRepository(impl: AnalyticsRepositoryImpl): AnalyticsRepository
}
