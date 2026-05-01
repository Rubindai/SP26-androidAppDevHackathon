package com.example.myapplication.data.remote

import com.example.myapplication.data.model.Courses

object CourseApi {
    val mockCourses = listOf(
        Courses(
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
            time = "10:10 AM - 11:00 AM",
            location = "Rhodes Hall 121",
            open = true
        ),
        Courses(
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
            time = "11:40 AM - 12:55 PM",
            location = "Uris Hall",
            open = false // testing "Closed"
        ),
        Courses(
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
            time = "11:40 AM - 12:55 PM",
            location = "Uris Hall",
            open = false
        ),
        Courses(
            name = "Intermediate Spanish Conversation",
            department = "SPAN",
            courseNumber = "2090",
            credits = 3,
            description = "Discussion-based practice for intermediate Spanish speakers.",
            prerequisites = listOf("SPAN 1230 or placement"),
            corequisites = emptyList(),
            distributions = listOf("ALC-AS"),
            year = 2026,
            semester = "Spring",
            instructor = "M. Garcia",
            days = "TR",
            time = "2:00 PM - 3:15 PM",
            location = "Goldwin Smith 132",
            open = true
        ),
        Courses(
            name = "Introductory Design and Programming",
            department = "INFO",
            courseNumber = "1300",
            credits = 3,
            description = "Foundations of design and programming for interactive systems.",
            prerequisites = emptyList(),
            corequisites = emptyList(),
            distributions = listOf("SBA-AS"),
            year = 2026,
            semester = "Spring",
            instructor = "A. Smith",
            days = "MWF",
            time = "1:25 PM - 2:15 PM",
            location = "Gates Hall G01",
            open = true
        ),
    )
}
