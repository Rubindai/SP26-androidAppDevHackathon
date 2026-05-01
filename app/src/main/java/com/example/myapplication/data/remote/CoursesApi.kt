package com.example.myapplication.data.remote

import com.example.myapplication.ui.components.courseInformation

object CourseApi {
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
            year = 2025, // testing if filtering by semester works
            semester = "Spring",
            instructor = "R. Baptist",
            days = "TR",
            time = "11:40 AM",
            location = "Uris Hall",
            open = false
        )
    )
}

