package com.classpoll.student.data.remote.api

import com.classpoll.student.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ClassroomApi {
    @POST("classrooms/join")
    suspend fun joinClassroom(@Body request: JoinClassroomRequest): Response<Classroom>

    @GET("classrooms/student")
    suspend fun getStudentClassrooms(): Response<List<Classroom>>

    @DELETE("classrooms/{id}/leave")
    suspend fun leaveClassroom(@Path("id") id: String): Response<Unit>
}
