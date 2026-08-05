package com.classpoll.student.data.remote.api

import com.classpoll.student.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface PollApi {
    @POST("polls/{id}/answer")
    suspend fun submitAnswer(
        @Path("id") pollId: String,
        @Body request: AnswerRequest
    ): Response<Unit>

    @GET("polls/{id}")
    suspend fun getPollById(@Path("id") id: String): Response<Poll>
}

data class AnswerRequest(
    @SerializedName("selected_indices") val selectedIndices: List<Int>,
    @SerializedName("response_time") val responseTime: Int
)
