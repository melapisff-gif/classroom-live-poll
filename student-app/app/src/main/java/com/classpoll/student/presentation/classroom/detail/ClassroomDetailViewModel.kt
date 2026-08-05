package com.classpoll.student.presentation.classroom.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classpoll.student.domain.model.Classroom
import com.classpoll.student.domain.repository.ClassroomRepository
import com.classpoll.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassroomDetailViewModel @Inject constructor(
    private val classroomRepository: ClassroomRepository
) : ViewModel() {

    private val _classroom = MutableStateFlow<Resource<Classroom>>(Resource.Loading)
    val classroom: StateFlow<Resource<Classroom>> = _classroom

    fun loadClassroom(classroomId: String) {
        viewModelScope.launch {
            _classroom.value = Resource.Loading
            val result = classroomRepository.getStudentClassrooms()
            if (result is Resource.Success) {
                val classroom = result.data.find { it.id == classroomId }
                if (classroom != null) {
                    _classroom.value = Resource.Success(classroom)
                } else {
                    _classroom.value = Resource.Error("Classroom not found")
                }
            } else {
                _classroom.value = Resource.Error("Failed to load classroom")
            }
        }
    }
}
