package com.classpoll.teacher.domain.repository

import com.classpoll.teacher.domain.model.ClassroomAnalytics
import com.classpoll.teacher.domain.model.PollAnalytics
import com.classpoll.teacher.utils.Resource

interface AnalyticsRepository {
    suspend fun getClassroomAnalytics(classroomId: String): Resource<ClassroomAnalytics>
    suspend fun getPollAnalytics(pollId: String): Resource<PollAnalytics>
}
