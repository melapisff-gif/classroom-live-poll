package com.classpoll.teacher.data.remote.dto

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
    val teacher: UserDto,
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

data class CreateClassroomRequest(
    val name: String,
    val description: String? = null
)

data class UpdateClassroomRequest(
    val name: String? = null,
    val description: String? = null
)

data class Classroom(
    val id: String,
    val name: String,
    val description: String? = null,
    @SerializedName("join_code") val joinCode: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("student_count") val studentCount: Int = 0,
    @SerializedName("poll_count") val pollCount: Int = 0
)

data class ClassroomDetail(
    val id: String,
    val name: String,
    val description: String? = null,
    @SerializedName("join_code") val joinCode: String,
    val members: List<ClassroomMember> = emptyList()
)

data class ClassroomMember(
    val id: String,
    val name: String,
    val email: String,
    @SerializedName("joined_at") val joinedAt: String
)

data class CreatePollRequest(
    @SerializedName("classroom_id") val classroomId: String,
    val question: String,
    @SerializedName("poll_type") val pollType: String,
    val options: List<OptionRequest>,
    val timer: Int
)

data class UpdatePollRequest(
    val question: String? = null,
    val options: List<OptionRequest>? = null
)

data class OptionRequest(
    val content: String,
    val index: Int
)

data class CorrectAnswerRequest(
    @SerializedName("correct_option_index") val correctOptionIndex: Int
)

data class Poll(
    val id: String,
    val question: String,
    @SerializedName("poll_type") val pollType: String,
    val options: List<PollOption>,
    val timer: Int,
    val status: String,
    @SerializedName("correct_option_index") val correctOptionIndex: Int? = null,
    @SerializedName("response_count") val responseCount: Int = 0
)

data class PollSummary(
    val id: String,
    val question: String,
    @SerializedName("poll_type") val pollType: String,
    val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("response_count") val responseCount: Int = 0
)

data class PollDetail(
    val id: String,
    val question: String,
    @SerializedName("poll_type") val pollType: String,
    val options: List<PollOption>,
    val timer: Int,
    val status: String,
    @SerializedName("correct_option_index") val correctOptionIndex: Int? = null,
    val responses: List<PollResponseDto> = emptyList()
)

data class PollOption(
    val id: String? = null,
    val content: String,
    val index: Int
)

data class PollResponseDto(
    val id: String,
    @SerializedName("student_id") val studentId: String,
    val student: UserDto,
    @SerializedName("selected_indices") val selectedIndices: List<Int>,
    @SerializedName("response_time") val responseTime: Int,
    @SerializedName("is_correct") val isCorrect: Boolean? = null
)

data class LeaderboardEntryDto(
    val rank: Int,
    @SerializedName("student_id") val studentId: String,
    @SerializedName("student_name") val studentName: String,
    @SerializedName("total_score") val totalScore: Int,
    @SerializedName("avg_response_time") val avgResponseTime: Int
)

data class ClassroomAnalyticsDto(
    @SerializedName("total_students") val totalStudents: Int,
    @SerializedName("correct_percentage") val correctPercentage: Double,
    @SerializedName("wrong_percentage") val wrongPercentage: Double,
    @SerializedName("avg_response_time") val avgResponseTime: Int,
    @SerializedName("option_wise_responses") val optionWiseResponses: List<OptionWiseResponseDto>,
    @SerializedName("most_difficult_question") val mostDifficultQuestion: PollAnalyticsQuestionDto?,
    @SerializedName("most_easy_question") val mostEasyQuestion: PollAnalyticsQuestionDto?
)

data class PollAnalyticsDto(
    @SerializedName("total_responses") val totalResponses: Int,
    @SerializedName("correct_count") val correctCount: Int,
    @SerializedName("wrong_count") val wrongCount: Int,
    @SerializedName("avg_response_time") val avgResponseTime: Int,
    @SerializedName("option_wise_responses") val optionWiseResponses: List<OptionWiseResponseDto>
)

data class OptionWiseResponseDto(
    @SerializedName("option_index") val optionIndex: Int,
    val count: Int
)

data class PollAnalyticsQuestionDto(
    val id: String,
    val question: String,
    @SerializedName("correct_percentage") val correctPercentage: Double
)
