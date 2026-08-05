package com.classpoll.student.domain.repository

import com.classpoll.student.domain.model.Classroom
import com.classpoll.student.utils.Resource

interface ClassroomRepository {
    suspend fun joinClassroom(joinCode: String): Resource<Classroom>
    suspend fun getStudentClassrooms(): Resource<List<Classroom>>
    suspend fun leaveClassroom(classroomId: String): Resource<Unit>
}
