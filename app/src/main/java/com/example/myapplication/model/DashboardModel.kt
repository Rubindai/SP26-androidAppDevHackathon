package com.example.myapplication.model

data class UserProgressState(
    val name: String,
    val major: String,
    val college: String,
    val gradYear : Int,
    val chosenSemester: String,
    val currentCredits: Int,
    val totalCredits: Int,
    val creditsNeeded: Int
)