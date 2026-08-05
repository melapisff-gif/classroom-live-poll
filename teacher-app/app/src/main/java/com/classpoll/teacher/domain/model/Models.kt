package com.classpoll.teacher.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String
)

data class Classroom(
    val id: String,
    val name: String,
    val description: String?,
    val joinCode: String,
    val createdAt: String,
    val studentCount: Int = 0
)

data class ClassroomDetail(
    val id: String,
    val name: String,
    val description: String?,
    val joinCode: String,
    val students: List<Student>
)

data class Student(
    val id: String,
    val name: String,
    val email: String,
    val joinedAt: String
)

data class Poll(
    val id: String,
    val question: String,
    val pollType: String,
    val options: List<PollOption>,
    val timer: Int,
    val status: String
)

data class PollSummary(
    val id: String,
    val question: String,
    val pollType: String,
    val status: String,
    val createdAt: String,
    val responseCount: Int
)

data class PollDetail(
    val id: String,
    val question: String,
    val pollType: String,
    val options: List<PollOption>,
    val timer: Int,
    val status: String,
    val correctOptionIndex: Int?
)

data class PollOption(
    val content: String,
    val index: Int
)

data class LeaderboardEntry(
    val rank: Int,
    val studentId: String,
    val studentName: String,
    val totalScore: Int,
    val avgResponseTime: Int
)

data class ClassroomAnalytics(
    val totalStudents: Int,
    val correctPercentage: Double,
    val wrongPercentage: Double,
    val avgResponseTime: Int,
    val optionWiseResponses: List<OptionWiseResponse>,
    val mostDifficultQuestion: PollAnalyticsQuestion?,
    val mostEasyQuestion: PollAnalyticsQuestion?
)

data class PollAnalytics(
    val totalResponses: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val avgResponseTime: Int,
    val optionWiseResponses: List<OptionWiseResponse>
)

data class OptionWiseResponse(
    val optionIndex: Int,
    val count: Int
)

data class PollAnalyticsQuestion(
    val id: String,
    val question: String,
    val correctPercentage: Double
)
