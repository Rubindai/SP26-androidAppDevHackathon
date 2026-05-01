package com.example.myapplication.data.remote.dto

import kotlinx.serialization.Serializable

// GET /courses/semesters  envelope
@Serializable
data class SemestersResponse(
    val semesters: List<String> = emptyList(),
)
