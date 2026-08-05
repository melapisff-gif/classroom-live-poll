package com.classpoll.student.presentation.classroom.join

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classpoll.student.domain.repository.ClassroomRepository
import com.classpoll.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinClassroomViewModel @Inject constructor(
    private val classroomRepository: ClassroomRepository
) : ViewModel() {

    var joinCode by mutableStateOf("")

    private val _uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading)
    val uiState: StateFlow<Resource<Unit>> = _uiState

    fun joinClassroom() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            val result = classroomRepository.joinClassroom(joinCode)
            _uiState.value = when (result) {
                is Resource.Success -> Resource.Success(Unit)
                is Resource.Error -> Resource.Error(result.message)
                is Resource.Loading -> Resource.Loading
            }
        }
    }
}
