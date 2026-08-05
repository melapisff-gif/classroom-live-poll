package com.classpoll.teacher.data.repository

import com.classpoll.teacher.data.remote.api.ClassroomApi
import com.classpoll.teacher.data.remote.dto.*
import com.classpoll.teacher.domain.model.Classroom as ClassroomModel
import com.classpoll.teacher.domain.model.ClassroomDetail as ClassroomDetailModel
import com.classpoll.teacher.domain.model.Student
import com.classpoll.teacher.domain.repository.ClassroomRepository
import com.classpoll.teacher.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassroomRepositoryImpl @Inject constructor(
    private val classroomApi: ClassroomApi
) : ClassroomRepository {

    override suspend fun createClassroom(name: String, description: String?): Resource<ClassroomModel> {
        return try {
            val response = classroomApi.createClassroom(CreateClassroomRequest(name, description))
            if (response.isSuccessful) {
                val body = response.body()!!
                Resource.Success(ClassroomModel(body.id, body.name, body.description, body.joinCode, body.createdAt, body.studentCount))
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getTeacherClassrooms(): Resource<List<ClassroomModel>> {
        return try {
            val response = classroomApi.getTeacherClassrooms()
            if (response.isSuccessful) {
                val classrooms = response.body()!!.map {
                    ClassroomModel(it.id, it.name, it.description, it.joinCode, it.createdAt, it.studentCount)
                }
                Resource.Success(classrooms)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getClassroomById(id: String): Resource<ClassroomDetailModel> {
        return try {
            val response = classroomApi.getClassroomById(id)
            if (response.isSuccessful) {
                val body = response.body()!!
                val students = body.members.map { Student(it.id, it.name, it.email, it.joinedAt) }
                Resource.Success(ClassroomDetailModel(body.id, body.name, body.description, body.joinCode, students))
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun deleteClassroom(id: String): Resource<Unit> {
        return try {
            val response = classroomApi.deleteClassroom(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun updateClassroom(id: String, name: String?, description: String?): Resource<ClassroomModel> {
        return try {
            val response = classroomApi.updateClassroom(id, UpdateClassroomRequest(name, description))
            if (response.isSuccessful) {
                val body = response.body()!!
                Resource.Success(ClassroomModel(body.id, body.name, body.description, body.joinCode, body.createdAt, body.studentCount))
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun removeStudent(classroomId: String, studentId: String): Resource<Unit> {
        return try {
            val response = classroomApi.removeStudent(classroomId, studentId)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
