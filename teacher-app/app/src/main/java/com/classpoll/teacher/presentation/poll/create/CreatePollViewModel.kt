package com.classpoll.teacher.presentation.poll.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classpoll.teacher.domain.repository.PollRepository
import com.classpoll.teacher.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePollViewModel @Inject constructor(
    private val pollRepository: PollRepository
) : ViewModel() {

    var question by mutableStateOf("")
    var pollType by mutableStateOf("SINGLE_CHOICE")
    var numberOfOptions by mutableIntStateOf(4)
    var timer by mutableIntStateOf(30)
    var options = mutableStateListOf("", "", "", "", "", "")

    private val _uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading)
    val uiState: StateFlow<Resource<Unit>> = _uiState

    fun updateOption(index: Int, value: String) {
        if (index < options.size) {
            options[index] = value
        }
    }

    fun createPoll(classroomId: String) {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            val validOptions = options.take(numberOfOptions).mapIndexed { index, content ->
                content to index
            }
            val result = pollRepository.createPoll(classroomId, question, pollType, validOptions, timer)
            _uiState.value = when (result) {
                is Resource.Success -> Resource.Success(Unit)
                is Resource.Error -> Resource.Error(result.message)
                is Resource.Loading -> Resource.Loading
            }
        }
    }
}
