package com.classpoll.student.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classpoll.student.domain.model.Classroom
import com.classpoll.student.domain.repository.AuthRepository
import com.classpoll.student.domain.repository.ClassroomRepository
import com.classpoll.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val classroomRepository: ClassroomRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _classrooms = MutableStateFlow<Resource<List<Classroom>>>(Resource.Loading)
    val classrooms: StateFlow<Resource<List<Classroom>>> = _classrooms

    private val _logoutState = MutableStateFlow<Resource<Unit>>(Resource.Loading)
    val logoutState: StateFlow<Resource<Unit>> = _logoutState

    init {
        loadClassrooms()
    }

    fun loadClassrooms() {
        viewModelScope.launch {
            _classrooms.value = Resource.Loading
            _classrooms.value = classroomRepository.getStudentClassrooms()
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = authRepository.logout()
        }
    }
}
