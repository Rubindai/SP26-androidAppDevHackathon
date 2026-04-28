package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Requirement
import com.example.myapplication.data.model.Student
import com.example.myapplication.data.remote.RequirementSource
import com.example.myapplication.data.remote.StudentApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepository @Inject constructor(
) {
    private val _currentStudent = MutableStateFlow(StudentApi.student)
    val currentStudent: StateFlow<Student> = _currentStudent.asStateFlow()

    // added a function to update the student
    fun updateStudent(newStudent: Student) {
        _currentStudent.value = newStudent
    }

    fun student(): Student = StudentApi.student
    fun requirements(): List<Requirement> = RequirementSource.requirements
    fun completedCredits(): Int = StudentApi.completedCredits
    fun targetTotalCredits(): Int = StudentApi.targetTotalCredits
}