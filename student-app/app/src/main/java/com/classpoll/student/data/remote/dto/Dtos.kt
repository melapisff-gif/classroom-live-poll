package com.classpoll.student.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RefreshTokenRequest(
    @SerializedName("refresh_token") val refreshToken: String
)

data class LogoutRequest(
    @SerializedName("refresh_token") val refreshToken: String
)

data class AuthResponse(
    val student: UserDto,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)

data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String
)

data class JoinClassroomRequest(
    @SerializedName("join_code") val joinCode: String
)

data class Classroom(
    val id: String,
    val name: String,
    val description: String? = null,
    @SerializedName("teacher_name") val teacherName: String,
    @SerializedName("joined_at") val joinedAt: String
)

data class Poll(
    val id: String,
    val question: String,
    @SerializedName("poll_type") val pollType: String,
    val options: List<PollOption>,
    val timer: Int,
    val status: String
)

data class PollOption(
    val content: String,
    val index: Int
)

data class PollResult(
    @SerializedName("poll_id") val pollId: String,
    @SerializedName("correct_option_index") val correctOptionIndex: Int,
    @SerializedName("is_correct") val isCorrect: Boolean,
    val score: Int,
    @SerializedName("response_time") val responseTime: Int
)

data class LeaderboardEntry(
    val rank: Int,
    @SerializedName("student_id") val studentId: String,
    @SerializedName("student_name") val studentName: String,
    @SerializedName("total_score") val totalScore: Int,
    @SerializedName("avg_response_time") val avgResponseTime: Int
)
