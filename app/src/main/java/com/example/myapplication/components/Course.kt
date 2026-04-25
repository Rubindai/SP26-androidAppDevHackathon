package com.example.myapplication.components

import androidx.compose.runtime.Composable

data class courseInformation(
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
)

@Composable
fun Course() {

}