package com.classpoll.teacher.data.remote.api

import com.classpoll.teacher.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ClassroomApi {
    @POST("classrooms")
    suspend fun createClassroom(@Body request: CreateClassroomRequest): Response<Classroom>

    @GET("classrooms/teacher")
    suspend fun getTeacherClassrooms(): Response<List<Classroom>>

    @GET("classrooms/{id}")
    suspend fun getClassroomById(@Path("id") id: String): Response<ClassroomDetail>

    @PUT("classrooms/{id}")
    suspend fun updateClassroom(@Path("id") id: String, @Body request: UpdateClassroomRequest): Response<Classroom>

    @DELETE("classrooms/{id}")
    suspend fun deleteClassroom(@Path("id") id: String): Response<Unit>

    @DELETE("classrooms/{classroomId}/students/{studentId}")
    suspend fun removeStudent(
        @Path("classroomId") classroomId: String,
        @Path("studentId") studentId: String
    ): Response<Unit>
}
