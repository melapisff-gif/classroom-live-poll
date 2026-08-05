package com.classpoll.student.presentation.poll.active

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classpoll.student.domain.model.Poll
import com.classpoll.student.domain.repository.PollRepository
import com.classpoll.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ActivePollViewModel @Inject constructor(
    private val pollRepository: PollRepository
) : ViewModel() {

    private val _poll = MutableStateFlow<Resource<Poll>>(Resource.Loading)
    val poll: StateFlow<Resource<Poll>> = _poll

    private val _remainingTime = MutableStateFlow(0)
    val remainingTime: StateFlow<Int> = _remainingTime

    private val _submitState = MutableStateFlow<Resource<Unit>>(Resource.Loading)
    val submitState: StateFlow<Resource<Unit>> = _submitState

    var selectedOption by mutableStateOf<Int?>(null)
    private var startTime = 0L
    private var timerJob: Job? = null

    fun loadPoll(pollId: String) {
        viewModelScope.launch {
            _poll.value = Resource.Loading
            val result = pollRepository.getPollById(pollId)
            _poll.value = result
            if (result is Resource.Success) {
                _remainingTime.value = result.data.timer
                startTime = System.currentTimeMillis()
                startTimer()
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value--
            }
        }
    }

    fun submitAnswer(pollId: String) {
        viewModelScope.launch {
            _submitState.value = Resource.Loading
            val responseTime = ((System.currentTimeMillis() - startTime) / 1000).toInt()
            val result = pollRepository.submitAnswer(pollId, listOf(selectedOption!!), responseTime)
            _submitState.value = result
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
