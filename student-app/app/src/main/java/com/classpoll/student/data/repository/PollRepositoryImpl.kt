package com.classpoll.student.data.repository

import com.classpoll.student.data.remote.api.AnswerRequest
import com.classpoll.student.data.remote.api.PollApi
import com.classpoll.student.domain.model.Poll
import com.classpoll.student.domain.model.PollOption
import com.classpoll.student.domain.repository.PollRepository
import com.classpoll.student.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PollRepositoryImpl @Inject constructor(
    private val pollApi: PollApi
) : PollRepository {

    override suspend fun getPollById(id: String): Resource<Poll> {
        return try {
            val response = pollApi.getPollById(id)
            if (response.isSuccessful) {
                val body = response.body()!!
                val options = body.options.map { PollOption(it.content, it.index) }
                Resource.Success(Poll(body.id, body.question, body.pollType, options, body.timer))
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun submitAnswer(pollId: String, selectedIndices: List<Int>, responseTime: Int): Resource<Unit> {
        return try {
            val response = pollApi.submitAnswer(pollId, AnswerRequest(selectedIndices, responseTime))
            if (response.isSuccessful) Resource.Success(Unit)
            else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
