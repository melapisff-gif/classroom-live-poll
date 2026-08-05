package com.classpoll.teacher.presentation.poll.edit

import androidx.lifecycle.SavedStateHandle
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
class EditPollViewModel @Inject constructor(
    private val pollRepository: PollRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pollId: String = savedStateHandle["pollId"] ?: ""

    var question = ""
        private set
    var options = mutableListOf("")
        private set

    private val _uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading)
    val uiState: StateFlow<Resource<Unit>> = _uiState

    private val _pollLoaded = MutableStateFlow(false)
    val pollLoaded: StateFlow<Boolean> = _pollLoaded

    init {
        loadPoll()
    }

    private fun loadPoll() {
        viewModelScope.launch {
            when (val result = pollRepository.getPollById(pollId)) {
                is Resource.Success -> {
                    val poll = result.data
                    question = poll.question
                    options = poll.options.map { it.content }.toMutableList()
                    _pollLoaded.value = true
                }
                is Resource.Error -> {
                    _uiState.value = Resource.Error(result.message)
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun onQuestionChange(value: String) { question = value }
    fun updateOption(index: Int, value: String) {
        while (options.size <= index) options.add("")
        options[index] = value
    }
    fun addOption() { options.add("") }
    fun removeOption(index: Int) { if (options.size > 2) options.removeAt(index) }

    fun updatePoll() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            val optionPairs = options.filter { it.isNotBlank() }.mapIndexed { index, content -> Pair(content, index) }
            val result = pollRepository.updatePoll(pollId, question.ifBlank { null }, optionPairs)
            when (result) {
                is Resource.Success -> _uiState.value = Resource.Success(Unit)
                is Resource.Error -> _uiState.value = Resource.Error(result.message)
                is Resource.Loading -> {}
            }
        }
    }
}
