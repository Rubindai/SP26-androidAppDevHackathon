package com.example.myapplication.data.model

data class Courses(
    val name: String,
    val department: String,
    val courseNumber: String,
    val credits: Int,
    val description: String,
    val prerequisites: List<String>,
    val corequisites: List<String>,
    val distributions: List<String>,
    val year: Int,
    val semester: String,
    val instructor: String,
    val days: String,
    val time: String,
    val location: String,
    val open: Boolean,
)

val Courses.id: String get() = "$department$courseNumber"
val Courses.code: String get() = "$department $courseNumber"
