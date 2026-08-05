package com.classpoll.teacher.presentation.classroom.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classpoll.teacher.domain.model.ClassroomDetail
import com.classpoll.teacher.domain.model.PollSummary
import com.classpoll.teacher.domain.repository.ClassroomRepository
import com.classpoll.teacher.domain.repository.PollRepository
import com.classpoll.teacher.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassroomDetailViewModel @Inject constructor(
    private val classroomRepository: ClassroomRepository,
    private val pollRepository: PollRepository
) : ViewModel() {

    private val _classroom = MutableStateFlow<Resource<ClassroomDetail>>(Resource.Loading)
    val classroom: StateFlow<Resource<ClassroomDetail>> = _classroom

    private val _polls = MutableStateFlow<Resource<List<PollSummary>>>(Resource.Loading)
    val polls: StateFlow<Resource<List<PollSummary>>> = _polls

    fun loadClassroom(classroomId: String) {
        viewModelScope.launch {
            _classroom.value = Resource.Loading
            _classroom.value = classroomRepository.getClassroomById(classroomId)
            loadPolls(classroomId)
        }
    }

    private fun loadPolls(classroomId: String) {
        viewModelScope.launch {
            _polls.value = Resource.Loading
            _polls.value = pollRepository.getClassroomPolls(classroomId)
        }
    }

    fun deleteClassroom(classroomId: String) {
        viewModelScope.launch {
            classroomRepository.deleteClassroom(classroomId)
        }
    }

    fun removeStudent(classroomId: String, studentId: String) {
        viewModelScope.launch {
            classroomRepository.removeStudent(classroomId, studentId)
            loadClassroom(classroomId)
        }
    }

    fun startPoll(pollId: String) {
        viewModelScope.launch {
            pollRepository.startPoll(pollId)
        }
    }

    fun deletePoll(pollId: String) {
        viewModelScope.launch {
            pollRepository.deletePoll(pollId)
            // Reload classroom to refresh polls
            val currentState = _classroom.value
            if (currentState is Resource.Success) {
                loadClassroom(currentState.data.id)
            }
        }
    }
}
