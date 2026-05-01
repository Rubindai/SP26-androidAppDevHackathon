package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.ProgressStatus
import com.example.myapplication.data.model.Requirement
import com.example.myapplication.data.repository.StudentRepository
import com.example.myapplication.data.repository.toRequirements
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
) : ViewModel() {

    data class ProgressUiState(
        val major: String = "",
        val requirements: List<Requirement> = emptyList(),
        val percent: Float = 0f,
        val complete: Int = 0,
        val inProgress: Int = 0,
        val left: Int = 0,
        val targetTerm: String = "",
        val onPaceTerm: String = "",
        val completedCredits: Int = 0,
        val totalCreditsTarget: Int = 0,
        val isLoading: Boolean = true,
        val error: String? = null,
    ) {
        val percentLabel: Int get() = (percent * 100).toInt()
    }

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Mirror the live user profile into the header fields the moment it changes.
        viewModelScope.launch {
            studentRepository.currentStudent.collect { student ->
                _uiState.value = _uiState.value.copy(
                    major = student.major.ifBlank { "Undeclared" },
                    targetTerm = student.targetTerm.ifBlank { "—" },
                    // Rough estimate: backend doesn't sum credits for us, so we approximate
                    // 3 credits per completed course. Replace once you have a credits-by-course
                    // index in the client (CourseRepository.courses cache covers FA26).
                    completedCredits = student.completed.size * 3,
                    totalCreditsTarget = student.targetCreditsHigh.takeIf { it > 0 } ?: 120,
                )
            }
        }

        // Also refresh from server in case we landed here from a relaunch (mock seed in
        // _currentStudent until then). Best-effort.
        viewModelScope.launch { studentRepository.refreshCurrentUser() }

        refresh()
    }

    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            studentRepository.fetchProgress()
                .onSuccess { response ->
                    val reqs = response.toRequirements()
                    val items = reqs.flatMap { it.items }
                    val complete = items.count { it.status == ProgressStatus.COMPLETE }
                    val inProgress = items.count { it.status == ProgressStatus.IN_PROGRESS }
                    val percent = if (items.isEmpty()) 0f else (complete + inProgress * 0.5f) / items.size
                    _uiState.value = _uiState.value.copy(
                        requirements = reqs,
                        percent = percent,
                        complete = complete,
                        inProgress = inProgress,
                        left = items.size - complete - inProgress,
                        isLoading = false,
                        error = null,
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load progress",
                    )
                }
        }
    }
}
