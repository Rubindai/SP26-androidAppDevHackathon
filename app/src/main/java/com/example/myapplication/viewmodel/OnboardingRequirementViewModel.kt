package com.example.myapplication.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Courses
import com.example.myapplication.data.model.code
import com.example.myapplication.data.repository.CourseRepository
import com.example.myapplication.data.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CompletedCoursesUiState(
    val query: String = "",
    val courses: List<Courses> = emptyList(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null,
    val saveError: String? = null,
)

@HiltViewModel
class OnboardingRequirementViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val courseRepository: CourseRepository,
) : ViewModel() {
    private val _completedCourseIds = mutableStateListOf<String>()
    val completedCourseIds: List<String> = _completedCourseIds

    private val _uiState = MutableStateFlow(CompletedCoursesUiState())
    val uiState = _uiState.asStateFlow()

    private var allCourses: List<Courses> = emptyList()

    init {
        refreshCourses()
    }

    fun refreshCourses() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            courseRepository.refresh()
                .onSuccess { courses ->
                    allCourses = courses.sortedBy { it.courseId }
                    updateFilteredCourses()
                    _uiState.update { it.copy(isLoading = false, error = null) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            courses = emptyList(),
                            isLoading = false,
                            error = e.message ?: "Failed to load courses",
                        )
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(query = query, saveError = null) }
        updateFilteredCourses()
    }

    fun toggleCourse(courseId: String) {
        if (_completedCourseIds.contains(courseId)) {
            _completedCourseIds.remove(courseId)
        } else {
            _completedCourseIds.add(courseId)
        }
        _uiState.update { it.copy(saveError = null) }
    }

    fun finishOnboarding(onDone: () -> Unit = {}) {
        val ids = _completedCourseIds.toList()
        _uiState.update { it.copy(isSaving = true, saveError = null) }
        viewModelScope.launch {
            val failures = ids.mapNotNull { id ->
                studentRepository.markCompleted(id).exceptionOrNull()?.let { id }
            }
            if (failures.isEmpty()) {
                _uiState.update { it.copy(isSaving = false, saveError = null) }
                onDone()
            } else {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saveError = "Could not save: ${failures.joinToString()}",
                    )
                }
            }
        }
    }

    private fun updateFilteredCourses() {
        val query = _uiState.value.query.trim()
        val filtered = if (query.isBlank()) {
            allCourses.take(80)
        } else {
            allCourses.filter { course ->
                course.courseId.contains(query, ignoreCase = true) ||
                    course.code.contains(query, ignoreCase = true) ||
                    course.name.contains(query, ignoreCase = true) ||
                    course.department.contains(query, ignoreCase = true) ||
                    course.courseNumber.contains(query, ignoreCase = true)
            }.take(100)
        }
        _uiState.update { it.copy(courses = filtered) }
    }
}
