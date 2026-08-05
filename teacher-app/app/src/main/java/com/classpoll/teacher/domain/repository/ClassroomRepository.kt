package com.classpoll.teacher.domain.repository

import com.classpoll.teacher.domain.model.Classroom
import com.classpoll.teacher.domain.model.ClassroomDetail
import com.classpoll.teacher.utils.Resource

interface ClassroomRepository {
    suspend fun createClassroom(name: String, description: String?): Resource<Classroom>
    suspend fun getTeacherClassrooms(): Resource<List<Classroom>>
    suspend fun getClassroomById(id: String): Resource<ClassroomDetail>
    suspend fun updateClassroom(id: String, name: String?, description: String?): Resource<Classroom>
    suspend fun deleteClassroom(id: String): Resource<Unit>
    suspend fun removeStudent(classroomId: String, studentId: String): Resource<Unit>
}
