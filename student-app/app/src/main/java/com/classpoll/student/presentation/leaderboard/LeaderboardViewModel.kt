package com.classpoll.student.presentation.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classpoll.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LeaderboardViewModel @Inject constructor() : ViewModel() {

    private val _leaderboard = MutableStateFlow<Resource<List<LeaderboardEntry>>>(Resource.Loading)
    val leaderboard: StateFlow<Resource<List<LeaderboardEntry>>> = _leaderboard

    fun loadLeaderboard(classroomId: String) {
        viewModelScope.launch {
            _leaderboard.value = Resource.Loading
            // TODO: Implement API call
            _leaderboard.value = Resource.Success(emptyList())
        }
    }
}
