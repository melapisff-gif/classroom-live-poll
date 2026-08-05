package com.classpoll.student.presentation.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classpoll.student.domain.repository.AuthRepository
import com.classpoll.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    private val _uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading)
    val uiState: StateFlow<Resource<Unit>> = _uiState

    init {
        checkLogin()
    }

    private fun checkLogin() {
        viewModelScope.launch {
            if (authRepository.isLoggedIn()) {
                _uiState.value = Resource.Success(Unit)
            } else {
                _uiState.value = Resource.Error("")
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            val result = authRepository.login(email, password)
            _uiState.value = when (result) {
                is Resource.Success -> Resource.Success(Unit)
                is Resource.Error -> Resource.Error(result.message)
                is Resource.Loading -> Resource.Loading
            }
        }
    }
}
