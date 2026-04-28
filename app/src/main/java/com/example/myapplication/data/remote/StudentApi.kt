package com.example.myapplication.data.remote

import com.example.myapplication.data.model.Student

object StudentApi {
    val student: Student = Student(
        name = "Avery Chen",
        initial = "AC",
        netid = "ac2847",
        major = "Undeclared (A&S)",
        year = "Sophomore",
        targetTerm = "Fall 2026",
        targetCreditsLow = 13,
        targetCreditsHigh = 17,
        completed = listOf("ENGL 1170", "MATH 1110", "PSYCH 1101", "CS 1110", "GOVT 1111"),
    )

    val completedCredits: Int = 47
    val targetTotalCredits: Int = 120
}
