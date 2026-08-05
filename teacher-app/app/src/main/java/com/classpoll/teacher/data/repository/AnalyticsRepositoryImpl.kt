package com.classpoll.teacher.data.repository

import com.classpoll.teacher.data.remote.api.AnalyticsApi
import com.classpoll.teacher.domain.model.ClassroomAnalytics
import com.classpoll.teacher.domain.model.OptionWiseResponse
import com.classpoll.teacher.domain.model.PollAnalytics
import com.classpoll.teacher.domain.model.PollAnalyticsQuestion
import com.classpoll.teacher.domain.repository.AnalyticsRepository
import com.classpoll.teacher.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepositoryImpl @Inject constructor(
    private val analyticsApi: AnalyticsApi
) : AnalyticsRepository {

    override suspend fun getClassroomAnalytics(classroomId: String): Resource<ClassroomAnalytics> {
        return try {
            val response = analyticsApi.getClassroomAnalytics(classroomId)
            if (response.isSuccessful) {
                val body = response.body()!!
                val optionWise = body.optionWiseResponses.map { OptionWiseResponse(it.optionIndex, it.count) }
                val mostDifficult = body.mostDifficultQuestion?.let {
                    PollAnalyticsQuestion(it.id, it.question, it.correctPercentage)
                }
                val mostEasy = body.mostEasyQuestion?.let {
                    PollAnalyticsQuestion(it.id, it.question, it.correctPercentage)
                }
                Resource.Success(
                    ClassroomAnalytics(
                        totalStudents = body.totalStudents,
                        correctPercentage = body.correctPercentage,
                        wrongPercentage = body.wrongPercentage,
                        avgResponseTime = body.avgResponseTime,
                        optionWiseResponses = optionWise,
                        mostDifficultQuestion = mostDifficult,
                        mostEasyQuestion = mostEasy
                    )
                )
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getPollAnalytics(pollId: String): Resource<PollAnalytics> {
        return try {
            val response = analyticsApi.getPollAnalytics(pollId)
            if (response.isSuccessful) {
                val body = response.body()!!
                val optionWise = body.optionWiseResponses.map { OptionWiseResponse(it.optionIndex, it.count) }
                Resource.Success(
                    PollAnalytics(
                        totalResponses = body.totalResponses,
                        correctCount = body.correctCount,
                        wrongCount = body.wrongCount,
                        avgResponseTime = body.avgResponseTime,
                        optionWiseResponses = optionWise
                    )
                )
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
