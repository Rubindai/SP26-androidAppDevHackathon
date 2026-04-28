package com.example.myapplication.data.model


data class Student(
    val name: String,
    val initial: String,
    val netid: String,
    val major: String,
    val year: String,
    val targetTerm: String,
    val targetCreditsLow: Int,
    val targetCreditsHigh: Int,
    val completed: List<String>,
)