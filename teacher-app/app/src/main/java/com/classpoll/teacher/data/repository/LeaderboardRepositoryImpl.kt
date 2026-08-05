package com.classpoll.teacher.data.repository

import com.classpoll.teacher.data.remote.api.LeaderboardApi
import com.classpoll.teacher.domain.model.LeaderboardEntry
import com.classpoll.teacher.domain.repository.LeaderboardRepository
import com.classpoll.teacher.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaderboardRepositoryImpl @Inject constructor(
    private val leaderboardApi: LeaderboardApi
) : LeaderboardRepository {

    override suspend fun getClassroomLeaderboard(classroomId: String): Resource<List<LeaderboardEntry>> {
        return try {
            val response = leaderboardApi.getClassroomLeaderboard(classroomId)
            if (response.isSuccessful) {
                val entries = response.body()!!.map {
                    LeaderboardEntry(it.rank, it.studentId, it.studentName, it.totalScore, it.avgResponseTime)
                }
                Resource.Success(entries)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getPollLeaderboard(pollId: String): Resource<List<LeaderboardEntry>> {
        return try {
            val response = leaderboardApi.getPollLeaderboard(pollId)
            if (response.isSuccessful) {
                val entries = response.body()!!.map {
                    LeaderboardEntry(it.rank, it.studentId, it.studentName, it.totalScore, it.avgResponseTime)
                }
                Resource.Success(entries)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
