package com.classpoll.student.presentation.poll.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classpoll.student.domain.model.PollResult
import com.classpoll.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PollResultViewModel @Inject constructor() : ViewModel() {

    private val _result = MutableStateFlow<Resource<PollResult>>(Resource.Loading)
    val result: StateFlow<Resource<PollResult>> = _result

    fun loadResult(pollId: String) {
        viewModelScope.launch {
            _result.value = Resource.Loading
            // In a real app, this would fetch from API
            _result.value = Resource.Success(
                PollResult(
                    pollId = pollId,
                    correctOptionIndex = 0,
                    isCorrect = false,
                    score = 0,
                    responseTime = 15
                )
            )
        }
    }
}
