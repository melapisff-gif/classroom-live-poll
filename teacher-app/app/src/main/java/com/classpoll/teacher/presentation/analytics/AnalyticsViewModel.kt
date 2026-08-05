package com.classpoll.teacher.presentation.analytics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classpoll.teacher.domain.model.ClassroomAnalytics
import com.classpoll.teacher.domain.repository.AnalyticsRepository
import com.classpoll.teacher.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val analyticsRepository: AnalyticsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val classroomId: String = savedStateHandle["classroomId"] ?: ""

    private val _analytics = MutableStateFlow<Resource<ClassroomAnalytics>>(Resource.Loading)
    val analytics: StateFlow<Resource<ClassroomAnalytics>> = _analytics

    init {
        loadAnalytics()
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            _analytics.value = Resource.Loading
            _analytics.value = analyticsRepository.getClassroomAnalytics(classroomId)
        }
    }

    fun refresh() {
        loadAnalytics()
    }
}
