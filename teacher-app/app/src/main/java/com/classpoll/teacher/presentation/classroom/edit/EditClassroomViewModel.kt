package com.classpoll.teacher.presentation.classroom.edit

import androidx.lifecycle.SavedStateHandle
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
class EditClassroomViewModel @Inject constructor(
    private val classroomRepository: ClassroomRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val classroomId: String = savedStateHandle["classroomId"] ?: ""

    var name = ""
        private set
    var description = ""
        private set

    private val _uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading)
    val uiState: StateFlow<Resource<Unit>> = _uiState

    private val _classroomLoaded = MutableStateFlow(false)
    val classroomLoaded: StateFlow<Boolean> = _classroomLoaded

    init {
        loadClassroom()
    }

    private fun loadClassroom() {
        viewModelScope.launch {
            when (val result = classroomRepository.getClassroomById(classroomId)) {
                is Resource.Success -> {
                    val classroom = result.data
                    name = classroom.name
                    description = classroom.description ?: ""
                    _classroomLoaded.value = true
                }
                is Resource.Error -> {
                    _uiState.value = Resource.Error(result.message)
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun onNameChange(value: String) { name = value }
    fun onDescriptionChange(value: String) { description = value }

    fun updateClassroom() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            val result = classroomRepository.updateClassroom(
                classroomId,
                name.ifBlank { null },
                description.ifBlank { null }
            )
            when (result) {
                is Resource.Success -> _uiState.value = Resource.Success(Unit)
                is Resource.Error -> _uiState.value = Resource.Error(result.message)
                is Resource.Loading -> {}
            }
        }
    }
}
