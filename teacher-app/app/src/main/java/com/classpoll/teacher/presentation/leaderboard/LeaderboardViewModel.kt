package com.classpoll.teacher.presentation.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classpoll.teacher.domain.model.LeaderboardEntry
import com.classpoll.teacher.domain.repository.LeaderboardRepository
import com.classpoll.teacher.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository
) : ViewModel() {

    private val _leaderboard = MutableStateFlow<Resource<List<LeaderboardEntry>>>(Resource.Loading)
    val leaderboard: StateFlow<Resource<List<LeaderboardEntry>>> = _leaderboard

    fun loadLeaderboard(classroomId: String) {
        viewModelScope.launch {
            _leaderboard.value = Resource.Loading
            _leaderboard.value = leaderboardRepository.getClassroomLeaderboard(classroomId)
        }
    }

    fun refresh(classroomId: String) {
        loadLeaderboard(classroomId)
    }
}
