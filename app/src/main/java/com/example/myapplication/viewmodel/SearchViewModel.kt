package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Courses
import com.example.myapplication.data.model.days
import com.example.myapplication.data.model.instructor
import com.example.myapplication.data.model.semester
import com.example.myapplication.data.model.time
import com.example.myapplication.data.model.year
import com.example.myapplication.data.repository.CourseRepository
import com.example.myapplication.data.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SearchUiState {
    data object Loading : SearchUiState
    data class Success(val courses: List<Courses>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val scheduleRepository: ScheduleRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _selectedSemester = MutableStateFlow("Fall 2026")
    val selectedSemester: StateFlow<String> = _selectedSemester.asStateFlow()

    private val _searchingText = MutableStateFlow("")
    val searchingText: StateFlow<String> = _searchingText.asStateFlow()

    private val _selectedFilters = MutableStateFlow<Map<String, String>>(emptyMap())
    val selectedFilters = _selectedFilters.asStateFlow()

    private val _filteredCourses = MutableStateFlow<List<Courses>>(emptyList())
    val filteredCourses: StateFlow<List<Courses>> = _filteredCourses.asStateFlow()

    val addedCourses: StateFlow<Set<String>> = scheduleRepository.addedIds

    private val _semesters = MutableStateFlow<List<String>>(listOf("Fall 2026"))
    val semesters: StateFlow<List<String>> = _semesters.asStateFlow()

    init {
        loadCourses()
        loadSemesters()
    }

    private fun loadSemesters() {
        viewModelScope.launch {
            courseRepository.listSemesters()
                .onSuccess { backendSems ->
                    val ui = backendSems.mapNotNull { uiSemester(it) }
                    if (ui.isNotEmpty()) _semesters.value = ui
                }
                .onFailure { Log.w("Search", "listSemesters failed: ${it.message}") }
        }
    }

    fun refresh() = loadCourses()

    private fun loadCourses() {
        _uiState.value = SearchUiState.Loading
        viewModelScope.launch {
            var lastError: Throwable? = null
            repeat(COURSE_LOAD_ATTEMPTS) { attempt ->
                val result = scheduleRepository.suggestions(limit = SUGGESTION_LIMIT)
                if (result.isSuccess) {
                    val courses = result.getOrThrow()
                    _uiState.value = SearchUiState.Success(courses)
                    filterCourses(_searchingText.value)
                    return@launch
                }
                lastError = result.exceptionOrNull()
                if (attempt < COURSE_LOAD_ATTEMPTS - 1) delay(COURSE_LOAD_RETRY_DELAY_MS)
            }
            _uiState.value = SearchUiState.Error(lastError?.message ?: "Failed to load courses")
        }
    }

    fun onSemesterChanged(newSemester: String) {
        _selectedSemester.value = newSemester
        loadCourses()
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchingText.value = newQuery
        filterCourses(newQuery)
    }

    fun addOrDeleteCourse(course: Courses) {
        viewModelScope.launch {
            scheduleRepository.toggleAddedRemote(course)
                .onFailure { e -> Log.w("Search", "Add/remove failed: ${e.message}") }
        }
    }

    fun dropdownFilter(category: String, option: String) {
        val updated = _selectedFilters.value.toMutableMap()
        if (updated[category] == option) updated.remove(category) else updated[category] = option
        _selectedFilters.value = updated
        filterCourses(_searchingText.value)
    }

    private fun filterCourses(searchBarText: String) {
        val source = (_uiState.value as? SearchUiState.Success)?.courses ?: emptyList()
        val filters = _selectedFilters.value

        _filteredCourses.value = source.filter { course ->
            val matchesSearch = searchBarText.isEmpty() ||
                course.name.contains(searchBarText, ignoreCase = true) ||
                course.department.contains(searchBarText, ignoreCase = true) ||
                course.courseNumber.contains(searchBarText, ignoreCase = true) ||
                course.instructor.contains(searchBarText, ignoreCase = true)

//            val matchesDist = filters["Distributions"]?.let { course.distributions.contains(it) } ?: true
            val matchesCredits = filters["Credits"]?.let { course.credits.toString() == it } ?: true
            val matchesSubject = filters["Subject"]?.let { course.department == it } ?: true

            val matchesLevels = when (filters["Level"]) {
                "1000s" -> course.courseNumber.startsWith("1")
                "2000s" -> course.courseNumber.startsWith("2")
                "3000s" -> course.courseNumber.startsWith("3")
                "4000+" -> course.courseNumber.firstOrNull()?.digitToIntOrNull()?.let { it >= 4 } ?: false
                null -> true
                else -> true
            }

            val matchesDays = when (filters["Days"]) {
                "M/W/F" -> course.days.any { it in "MWF" }
                "Tu/Th" -> course.days.any { it in "TR" }
                null -> true
                else -> true
            }

            val matchesTime = when (filters["Time"]) {
                "8AM - 11:59AM" -> course.time.contains("AM")
                "12PM - 4:59PM", "5PM - 11:59PM" -> course.time.contains("PM")
                null -> true
                else -> true
            }

            matchesSearch && matchesCredits && matchesSubject &&
                matchesLevels && matchesDays && matchesTime
        }
    }

    // "Fall 2026" -> "FA26", "Spring 2026" -> "SP26"
    private fun backendSemester(uiSemester: String): String? {
        val parts = uiSemester.split(" ")
        if (parts.size != 2) return null
        val prefix = when (parts[0]) {
            "Fall" -> "FA"
            "Spring" -> "SP"
            "Summer" -> "SU"
            "Winter" -> "WI"
            else -> return null
        }
        val yy = parts[1].toIntOrNull()?.let { it % 100 }?.toString()?.padStart(2, '0') ?: return null
        return "$prefix$yy"
    }

    // "FA26" -> "Fall 2026", "SP27" -> "Spring 2027"
    private fun uiSemester(backend: String): String? {
        if (backend.length < 3) return null
        val prefix = backend.take(2).uppercase()
        val yy = backend.drop(2).toIntOrNull() ?: return null
        val term = when (prefix) {
            "FA" -> "Fall"
            "SP" -> "Spring"
            "SU" -> "Summer"
            "WI" -> "Winter"
            else -> return null
        }
        return "$term ${2000 + yy}"
    }

    private companion object {
        const val SUGGESTION_LIMIT = 25
        const val COURSE_LOAD_ATTEMPTS = 2
        const val COURSE_LOAD_RETRY_DELAY_MS = 700L
    }
}
