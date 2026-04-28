package com.example.myapplication.data.model


data class Student(
    val name: String = "",
    val initial: String = "",
    val netid: String = "",
    val major: String = "",
    val year: String = "",
    val targetTerm: String = "",
    val targetCreditsLow: Int = 0,
    val targetCreditsHigh: Int = 120,
    val completed: List<String> = emptyList(),
    val college: String = "",
)