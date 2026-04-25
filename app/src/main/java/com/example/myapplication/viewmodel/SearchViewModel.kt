package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.components.courseInformation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// In com.example.myapplication.components or a separate Data file
val mockCourses = listOf(
    courseInformation(
        name = "Intermediate Design & Programming for the Web",
        department = "INFO",
        courseNumber = "2300",
        credits = 4,
        description = "Web design and programming.",
        prerequisites = listOf("INFO 1300"),
        corequisites = emptyList(),
        distributions = listOf("ALC-AS", "SBA-AS"),
        year = 2026,
        semester = "Spring",
        instructor = "D. Schneider",
        days = "MWF",
        time = "10:10 AM",
        location = "Rhodes Hall 121",
        open = true
    ),
    courseInformation(
        name = "Introduction to U.S. History",
        department = "HIST",
        courseNumber = "1530",
        credits = 4,
        description = "A survey of American history.",
        prerequisites = emptyList(),
        corequisites = emptyList(),
        distributions = listOf("HA-AS"),
        year = 2026,
        semester = "Spring",
        instructor = "R. Baptist",
        days = "TR",
        time = "11:40 AM",
        location = "Uris Hall",
        open = false // Testing "Closed" status
    )
)


class SearchViewModel: ViewModel() {
    private val allCourses = mockCourses // we should get this from the backend

    private val _searchingText = MutableStateFlow("") // the text the user types into the search bar

    val searchingText: StateFlow<String> = _searchingText.asStateFlow()

    // The list that the UI observes
    private val _filteredCourses = MutableStateFlow(allCourses)
    val filteredCourses: StateFlow<List<courseInformation>> = _filteredCourses.asStateFlow()

    fun onSearchQueryChanged(newQuery: String) {
        _searchingText.value = newQuery
        filterCourses(newQuery)
    }

    private fun filterCourses(query: String) {
        if (query.isEmpty()) {
            _filteredCourses.value = allCourses
        } else {
            _filteredCourses.value = allCourses.filter { course ->
                course.name.contains(query, ignoreCase = true) || // name
                        course.department.contains(query, ignoreCase = true) || // department
                        course.courseNumber.contains(query, ignoreCase = true) // course number
                // add more here
            }
        }
    }
}