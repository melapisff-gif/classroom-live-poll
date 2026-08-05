package com.classpoll.teacher.data.remote.api

import com.classpoll.teacher.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface PollApi {
    @POST("polls")
    suspend fun createPoll(@Body request: CreatePollRequest): Response<Poll>

    @GET("polls/classroom/{classroomId}")
    suspend fun getClassroomPolls(@Path("classroomId") classroomId: String): Response<List<PollSummary>>

    @GET("polls/{id}")
    suspend fun getPollById(@Path("id") id: String): Response<PollDetail>

    @PUT("polls/{id}")
    suspend fun updatePoll(@Path("id") id: String, @Body request: UpdatePollRequest): Response<Poll>

    @DELETE("polls/{id}")
    suspend fun deletePoll(@Path("id") id: String): Response<Unit>

    @POST("polls/{id}/start")
    suspend fun startPoll(@Path("id") id: String): Response<Unit>

    @POST("polls/{id}/pause")
    suspend fun pausePoll(@Path("id") id: String): Response<Unit>

    @POST("polls/{id}/resume")
    suspend fun resumePoll(@Path("id") id: String): Response<Unit>

    @POST("polls/{id}/stop")
    suspend fun stopPoll(@Path("id") id: String): Response<Unit>

    @POST("polls/{id}/correct-answer")
    suspend fun setCorrectAnswer(@Path("id") id: String, @Body request: CorrectAnswerRequest): Response<Unit>

    @POST("polls/{id}/publish")
    suspend fun publishResults(@Path("id") id: String): Response<Unit>
}
