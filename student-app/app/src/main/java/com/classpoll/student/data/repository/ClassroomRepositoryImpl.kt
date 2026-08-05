package com.classpoll.student.data.repository

import com.classpoll.student.data.remote.api.ClassroomApi
import com.classpoll.student.data.remote.dto.*
import com.classpoll.student.domain.model.Classroom as ClassroomModel
import com.classpoll.student.domain.repository.ClassroomRepository
import com.classpoll.student.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassroomRepositoryImpl @Inject constructor(
    private val classroomApi: ClassroomApi
) : ClassroomRepository {

    override suspend fun joinClassroom(joinCode: String): Resource<ClassroomModel> {
        return try {
            val response = classroomApi.joinClassroom(JoinClassroomRequest(joinCode))
            if (response.isSuccessful) {
                val body = response.body()!!
                Resource.Success(ClassroomModel(body.id, body.name, body.teacherName, body.joinedAt))
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getStudentClassrooms(): Resource<List<ClassroomModel>> {
        return try {
            val response = classroomApi.getStudentClassrooms()
            if (response.isSuccessful) {
                val classrooms = response.body()!!.map {
                    ClassroomModel(it.id, it.name, it.teacherName, it.joinedAt)
                }
                Resource.Success(classrooms)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun leaveClassroom(classroomId: String): Resource<Unit> {
        return try {
            val response = classroomApi.leaveClassroom(classroomId)
            if (response.isSuccessful) Resource.Success(Unit)
            else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
