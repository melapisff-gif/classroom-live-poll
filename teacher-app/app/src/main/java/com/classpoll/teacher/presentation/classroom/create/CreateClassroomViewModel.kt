package com.classpoll.teacher.presentation.classroom.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classpoll.teacher.domain.repository.ClassroomRepository
import com.classpoll.teacher.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateClassroomViewModel @Inject constructor(
    private val classroomRepository: ClassroomRepository
) : ViewModel() {

    var name by mutableStateOf("")
    var description by mutableStateOf("")

    private val _uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading)
    val uiState: StateFlow<Resource<Unit>> = _uiState

    fun createClassroom() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            val result = classroomRepository.createClassroom(name, description.ifBlank { null })
            _uiState.value = when (result) {
                is Resource.Success -> Resource.Success(Unit)
                is Resource.Error -> Resource.Error(result.message)
                is Resource.Loading -> Resource.Loading
            }
        }
    }
}
