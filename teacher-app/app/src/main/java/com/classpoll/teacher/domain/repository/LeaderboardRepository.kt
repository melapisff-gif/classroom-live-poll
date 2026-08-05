package com.classpoll.teacher.domain.repository

import com.classpoll.teacher.domain.model.LeaderboardEntry
import com.classpoll.teacher.utils.Resource

interface LeaderboardRepository {
    suspend fun getClassroomLeaderboard(classroomId: String): Resource<List<LeaderboardEntry>>
    suspend fun getPollLeaderboard(pollId: String): Resource<List<LeaderboardEntry>>
}
