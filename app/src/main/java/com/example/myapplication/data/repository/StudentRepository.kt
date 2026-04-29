package com.example.myapplication.data.repository

import com.example.myapplication.components.courseInformation
import com.example.myapplication.data.model.Requirement
import com.example.myapplication.data.model.Student
import com.example.myapplication.data.remote.CourseApi
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
//        This can be used after we are provided with the API
//        viewModelScope.launch {
//            retroFitApi.postStudentProfile(newStudent)
//        }
    }

    fun student(): Student = StudentApi.student
    fun requirements(): List<Requirement> = RequirementSource.requirements
    fun completedCredits(): Int = StudentApi.completedCredits
    fun targetTotalCredits(): Int = StudentApi.targetTotalCredits

    fun getAllCourses(): List<courseInformation> {
        return CourseApi.mockCourses
    }
}