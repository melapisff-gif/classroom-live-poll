package com.classpoll.teacher.data.repository

import com.classpoll.teacher.data.remote.api.PollApi
import com.classpoll.teacher.data.remote.dto.*
import com.classpoll.teacher.domain.model.Poll as PollModel
import com.classpoll.teacher.domain.model.PollSummary as PollSummaryModel
import com.classpoll.teacher.domain.model.PollDetail as PollDetailModel
import com.classpoll.teacher.domain.model.PollOption as PollOptionModel
import com.classpoll.teacher.domain.repository.PollRepository
import com.classpoll.teacher.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PollRepositoryImpl @Inject constructor(
    private val pollApi: PollApi
) : PollRepository {

    override suspend fun createPoll(
        classroomId: String,
        question: String,
        pollType: String,
        options: List<Pair<String, Int>>,
        timer: Int
    ): Resource<PollModel> {
        return try {
            val optionRequests = options.map { OptionRequest(it.first, it.second) }
            val response = pollApi.createPoll(CreatePollRequest(classroomId, question, pollType, optionRequests, timer))
            if (response.isSuccessful) {
                val body = response.body()!!
                val pollOptions = body.options.map { PollOptionModel(it.content, it.index) }
                Resource.Success(PollModel(body.id, body.question, body.pollType, pollOptions, body.timer, body.status))
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getClassroomPolls(classroomId: String): Resource<List<PollSummaryModel>> {
        return try {
            val response = pollApi.getClassroomPolls(classroomId)
            if (response.isSuccessful) {
                val polls = response.body()!!.map {
                    PollSummaryModel(it.id, it.question, it.pollType, it.status, it.createdAt, it.responseCount)
                }
                Resource.Success(polls)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getPollById(id: String): Resource<PollDetailModel> {
        return try {
            val response = pollApi.getPollById(id)
            if (response.isSuccessful) {
                val body = response.body()!!
                val options = body.options.map { PollOptionModel(it.content, it.index) }
                Resource.Success(
                    PollDetailModel(body.id, body.question, body.pollType, options, body.timer, body.status, body.correctOptionIndex)
                )
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun deletePoll(id: String): Resource<Unit> {
        return try {
            val response = pollApi.deletePoll(id)
            if (response.isSuccessful) Resource.Success(Unit)
            else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun updatePoll(id: String, question: String?, options: List<Pair<String, Int>>?): Resource<PollModel> {
        return try {
            val optionRequests = options?.map { OptionRequest(it.first, it.second) }
            val response = pollApi.updatePoll(id, UpdatePollRequest(question, optionRequests))
            if (response.isSuccessful) {
                val body = response.body()!!
                val pollOptions = body.options.map { PollOptionModel(it.content, it.index) }
                Resource.Success(PollModel(body.id, body.question, body.pollType, pollOptions, body.timer, body.status))
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun startPoll(id: String): Resource<Unit> {
        return try {
            val response = pollApi.startPoll(id)
            if (response.isSuccessful) Resource.Success(Unit)
            else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun pausePoll(id: String): Resource<Unit> {
        return try {
            val response = pollApi.pausePoll(id)
            if (response.isSuccessful) Resource.Success(Unit)
            else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun resumePoll(id: String): Resource<Unit> {
        return try {
            val response = pollApi.resumePoll(id)
            if (response.isSuccessful) Resource.Success(Unit)
            else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun stopPoll(id: String): Resource<Unit> {
        return try {
            val response = pollApi.stopPoll(id)
            if (response.isSuccessful) Resource.Success(Unit)
            else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun setCorrectAnswer(pollId: String, optionIndex: Int): Resource<Unit> {
        return try {
            val response = pollApi.setCorrectAnswer(pollId, CorrectAnswerRequest(optionIndex))
            if (response.isSuccessful) Resource.Success(Unit)
            else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun publishResults(pollId: String): Resource<Unit> {
        return try {
            val response = pollApi.publishResults(pollId)
            if (response.isSuccessful) Resource.Success(Unit)
            else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
