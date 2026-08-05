package com.classpoll.teacher.presentation.classroom.joincode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classpoll.teacher.domain.model.ClassroomDetail
import com.classpoll.teacher.domain.repository.ClassroomRepository
import com.classpoll.teacher.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinCodeViewModel @Inject constructor(
    private val classroomRepository: ClassroomRepository
) : ViewModel() {

    private val _classroom = MutableStateFlow<Resource<ClassroomDetail>>(Resource.Loading)
    val classroom: StateFlow<Resource<ClassroomDetail>> = _classroom

    fun loadClassroom(classroomId: String) {
        viewModelScope.launch {
            _classroom.value = Resource.Loading
            _classroom.value = classroomRepository.getClassroomById(classroomId)
        }
    }
}
