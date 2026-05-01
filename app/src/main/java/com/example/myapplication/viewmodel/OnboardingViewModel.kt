package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Student
import com.example.myapplication.data.repository.ScheduleRepository
import com.example.myapplication.data.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface OnboardingUiState {
    data object Idle : OnboardingUiState
    data object Submitting : OnboardingUiState
    data class Error(val message: String) : OnboardingUiState
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val scheduleRepository: ScheduleRepository,
) : ViewModel() {

    private val _studentState = MutableStateFlow(Student())
    val studentState: StateFlow<Student> = _studentState.asStateFlow()

    private val _uiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Idle)
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _studentState.value = _studentState.value.copy(name = name)
    }

    fun updateMajor(major: String) {
        _studentState.value = _studentState.value.copy(major = normalizeSupportedMajor(major) ?: major)
    }

    fun updateYear(year: String) {
        _studentState.value = _studentState.value.copy(year = year)
    }

    fun updateNetId(netid: String) {
        _studentState.value = _studentState.value.copy(netid = netid.trim())
    }

    fun updateCollege(college: String) {
        _studentState.value = _studentState.value.copy(college = college)
    }

    fun continueFromBasicInfo(onComplete: () -> Unit) {
        val student = _studentState.value
        when {
            student.name.isBlank() || student.netid.isBlank() -> {
                _uiState.value = OnboardingUiState.Error("Name and NetID are required")
            }
            student.year.toIntOrNull() == null -> {
                _uiState.value = OnboardingUiState.Error("Enter a valid graduating year")
            }
            else -> {
                _uiState.value = OnboardingUiState.Idle
                onComplete()
            }
        }
    }

    // POST /users/ → save user_id, then ensure a schedule exists for FA26 → save schedule_id.
    fun finishOnboarding(onComplete: () -> Unit) {
        val student = _studentState.value
        val major = normalizeSupportedMajor(student.major)
        if (major == null) {
            _uiState.value = OnboardingUiState.Error("Select a supported major")
            return
        }
        _uiState.value = OnboardingUiState.Submitting
        viewModelScope.launch {
            val signInResult = studentRepository.signIn(student.copy(major = major))
            signInResult
                .onSuccess { userId ->
                    // Best-effort bootstrap; if the schedule call fails, we still let the user
                    // through onboarding. They can retry from the schedule screen.
                    scheduleRepository.bootstrap(userId)
                        .onFailure { e -> Log.w("Onboarding", "Schedule bootstrap failed: ${e.message}") }
                    _uiState.value = OnboardingUiState.Idle
                    onComplete()
                }
                .onFailure { e ->
                    _uiState.value = OnboardingUiState.Error(e.message ?: "Sign-in failed")
                }
        }
    }
}
