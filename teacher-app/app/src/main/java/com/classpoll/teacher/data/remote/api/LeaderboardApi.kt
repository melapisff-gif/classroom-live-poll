package com.classpoll.teacher.data.remote.api

import com.classpoll.teacher.data.remote.dto.LeaderboardEntryDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LeaderboardApi {
    @GET("leaderboard/classroom/{classroomId}")
    suspend fun getClassroomLeaderboard(@Path("classroomId") classroomId: String): Response<List<LeaderboardEntryDto>>

    @GET("leaderboard/poll/{pollId}")
    suspend fun getPollLeaderboard(@Path("pollId") pollId: String): Response<List<LeaderboardEntryDto>>
}
