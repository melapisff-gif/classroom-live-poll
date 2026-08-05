package com.classpoll.teacher.data.remote.api

import com.classpoll.teacher.data.remote.dto.ClassroomAnalyticsDto
import com.classpoll.teacher.data.remote.dto.PollAnalyticsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AnalyticsApi {
    @GET("analytics/classroom/{classroomId}")
    suspend fun getClassroomAnalytics(@Path("classroomId") classroomId: String): Response<ClassroomAnalyticsDto>

    @GET("analytics/poll/{pollId}")
    suspend fun getPollAnalytics(@Path("pollId") pollId: String): Response<PollAnalyticsDto>
}
