package com.classpoll.teacher.presentation.poll.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classpoll.teacher.data.remote.SocketManager
import com.classpoll.teacher.domain.model.PollDetail
import com.classpoll.teacher.domain.repository.PollRepository
import com.classpoll.teacher.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LivePollViewModel @Inject constructor(
    private val pollRepository: PollRepository,
    private val socketManager: SocketManager
) : ViewModel() {

    private val _poll = MutableStateFlow<Resource<PollDetail>>(Resource.Loading)
    val poll: StateFlow<Resource<PollDetail>> = _poll

    private val _responseCount = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val responseCount: StateFlow<Map<Int, Int>> = _responseCount

    private val _remainingTime = MutableStateFlow(0)
    val remainingTime: StateFlow<Int> = _remainingTime

    var selectedCorrectIndex by mutableStateOf(0)
    private var timerJob: Job? = null
    private var classroomId: String = ""

    fun loadPoll(pollId: String, classroomId: String = "") {
        this.classroomId = classroomId
        viewModelScope.launch {
            _poll.value = Resource.Loading
            val result = pollRepository.getPollById(pollId)
            _poll.value = result
            if (result is Resource.Success) {
                _remainingTime.value = result.data.timer
                startTimer()
                connectSocket()
            }
        }
    }

    private fun connectSocket() {
        if (classroomId.isNotEmpty()) {
            socketManager.connect(classroomId) { responseCountMap ->
                _responseCount.value = responseCountMap
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

    fun pausePoll(pollId: String) {
        timerJob?.cancel()
        viewModelScope.launch { pollRepository.pausePoll(pollId) }
    }

    fun resumePoll(pollId: String) {
        startTimer()
        viewModelScope.launch { pollRepository.resumePoll(pollId) }
    }

    fun stopPoll(pollId: String) {
        timerJob?.cancel()
        viewModelScope.launch { pollRepository.stopPoll(pollId) }
    }

    fun setCorrectAnswer(pollId: String) {
        viewModelScope.launch { pollRepository.setCorrectAnswer(pollId, selectedCorrectIndex) }
    }

    fun publishResults(pollId: String) {
        viewModelScope.launch { pollRepository.publishResults(pollId) }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        if (classroomId.isNotEmpty()) {
            socketManager.disconnect(classroomId)
        }
    }
}
