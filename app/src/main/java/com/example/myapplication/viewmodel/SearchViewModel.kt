package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.components.courseInformation
import com.example.myapplication.data.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
        open = false // testing "Closed"
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
        year = 2025,            // testing if filtering by semester works
        semester = "Spring",
        instructor = "R. Baptist",
        days = "TR",
        time = "11:40 AM",
        location = "Uris Hall",
        open = false
    )
)


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val studentRepository: StudentRepository
) : ViewModel() {
    private val allCourses = mockCourses // we should get this from the backend

    private val _selectedSemester =  MutableStateFlow("Spring 2026")
    val selectedSemester: StateFlow<String> = _selectedSemester.asStateFlow()

    private val _searchingText = MutableStateFlow("") // the text the user types into the search bar

    // this is for the dropdown filters
    // Using Map<String, String> like this: "Credits" -> "4", "Distributions" -> "HA-AS"
    private val _selectedFilters = MutableStateFlow<Map<String, String>>(emptyMap())

    val selectedFilters = _selectedFilters.asStateFlow()
    val searchingText: StateFlow<String> = _searchingText.asStateFlow()

    private val _filteredCourses = MutableStateFlow(allCourses)
    val filteredCourses: StateFlow<List<courseInformation>> = _filteredCourses.asStateFlow()

    fun onSemesterChanged(newSemester: String) {
        _selectedSemester.value = newSemester
        filterCourses(_searchingText.value)
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchingText.value = newQuery
        filterCourses(newQuery)
    }

    // tracks added courses. We will use courseIDs (Department + Number)
    private val _addedCourses = MutableStateFlow<Set<String>>(emptySet())
    val addedCourses: StateFlow<Set<String>> = _addedCourses.asStateFlow()

    // the list of courses added to the schedule
    private val _userSchedule = MutableStateFlow<List<courseInformation>>(emptyList())
    val userSchedule: StateFlow<List<courseInformation>> = _userSchedule.asStateFlow()

    // adds or deletes a course from the user's data
    fun addOrDeleteCourse(course: courseInformation) {
        val currentAdded = _addedCourses.value.toMutableSet()
        val currentSchedule = _userSchedule.value.toMutableList()

        // department + number (ex: "HIST1530")
        val courseId = "${course.department}${course.courseNumber}"

        if (currentAdded.contains(courseId)) {
            // remove course
            currentAdded.remove(courseId)
            currentSchedule.removeAll { "${it.department}${it.courseNumber}" == courseId }
        } else {
            // add course
            currentAdded.add(courseId)
            currentSchedule.add(course)
        }

        // update + tell UI about it
        _addedCourses.value = currentAdded
        _userSchedule.value = currentSchedule
    }

    fun dropdownFilter(category: String, option: String) {
        // get all selections
        val currentlySelected =
            _selectedFilters.value.toMutableMap() // this makes the selections editable

        if (currentlySelected[category] == option) { // this will mirror the logic of the prototype
            currentlySelected.remove(category) // where if the user clicks on the same option
        } else {                                     // twice, it will be removed
            currentlySelected[category] = option
        }

        _selectedFilters.value = currentlySelected // save selections
        filterCourses(searchingText.value) // update the list of courses based on the selections
    }

    private fun filterCourses(searchBarText: String) {
        val filters = _selectedFilters.value // get filters
        val currentTerm = _selectedSemester.value // get the semester


        _filteredCourses.value = allCourses.filter { course ->
            val term = currentTerm.split(" ")

            val matchesSemester = course.semester == term[0] && course.year == term[1].toInt()

            // search bar logic
            var matchesSearch = false
            if (searchBarText.isEmpty()) {
                matchesSearch = true
            } else {
                matchesSearch = course.name.contains(searchBarText, ignoreCase = true)
                        || course.department.contains(searchBarText, ignoreCase = true)
                        || course.courseNumber.contains(searchBarText, ignoreCase = true)
                        || course.instructor.contains(searchBarText, ignoreCase = true)
            }

            // dropdown logic
            var matchesDist = false
            if (filters.containsKey("Distributions")) {
                matchesDist = course.distributions.contains(filters["Distributions"])
            } else {
                matchesDist = true
            }

            var matchesCredits = false

            if (filters.containsKey("Credits")) {
                matchesCredits = (course.credits.toString() == filters["Credits"])
            } else {
                matchesCredits = true
            }

            var matchesLevels = false
            if (filters.containsKey("Level")) {
                val selected = filters["Level"]
                if (selected == "1000s") {
                    matchesLevels = course.courseNumber.startsWith("1")
                } else if (selected == "2000s") {
                    matchesLevels = course.courseNumber.startsWith("2")
                } else if (selected == "3000s") {
                    matchesLevels = course.courseNumber.startsWith("3")
                } else if (selected == "4000+") {
                    matchesLevels = course.courseNumber.first().digitToInt() >= 4
                } else {
                    matchesLevels = true
                }
            } else {
                matchesLevels = true
            }

            var matchesSubject = false
            if (filters.containsKey("Subject")) {
                matchesSubject = (course.department == filters["Subject"])
            } else {
                matchesSubject = true
            }

            var matchesDays = false
            if (filters.containsKey("Days")) {
                val selected = filters["Days"]
                if (selected == "M/W/F") {
                    matchesDays = course.days.contains("M")
                            || course.days.contains("W")
                            || course.days.contains("F")
                } else if (selected == "Tu/Th") {
                    matchesDays = course.days.contains("T")  // Assuming T is Tue and R is Thur
                            || course.days.contains("R")
                } else {
                    matchesDays = true
                }
            } else {
                matchesDays = true
            }

            var matchesTime = false
            if (filters.containsKey("Time")) {
                val selected = filters["Time"]
                if (selected == "8AM - 11:59AM") {
                    matchesTime = course.time.contains("AM") // idk how the time will be denoted
                } else if (selected == "12PM - 4:59PM" || selected == "5PM - 11:59PM") {
                    matchesTime = course.time.contains("PM")
                } else {
                    matchesTime = true
                }
            } else {
                matchesTime = true
            }

            matchesSemester && matchesSearch && matchesDist && matchesCredits && matchesSubject && matchesDays
                    && matchesTime && matchesLevels
        }
    }

    fun getAllSemesters(): List<String> {
        return listOf(
            "Fall 2024", "Spring 2025",
            "Fall 2025", "Spring 2026",
            "Fall 2026", "Spring 2027",
            "Fall 2027", "Spring 2028"
        )
    }
}