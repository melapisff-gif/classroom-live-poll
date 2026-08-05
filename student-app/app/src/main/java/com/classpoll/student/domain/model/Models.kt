package com.classpoll.student.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String
)

data class Classroom(
    val id: String,
    val name: String,
    val teacherName: String,
    val joinedAt: String
)

data class Poll(
    val id: String,
    val question: String,
    val pollType: String,
    val options: List<PollOption>,
    val timer: Int
)

data class PollOption(
    val content: String,
    val index: Int
)

data class PollResult(
    val pollId: String,
    val correctOptionIndex: Int,
    val isCorrect: Boolean,
    val score: Int,
    val responseTime: Int
)

data class LeaderboardEntry(
    val rank: Int,
    val studentName: String,
    val totalScore: Int,
    val avgResponseTime: Int
)
