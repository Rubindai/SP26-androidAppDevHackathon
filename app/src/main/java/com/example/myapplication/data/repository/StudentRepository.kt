package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Requirement
import com.example.myapplication.data.model.Student
import com.example.myapplication.data.remote.RequirementSource
import com.example.myapplication.data.remote.StudentApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepository @Inject constructor(
) {
    fun student(): Student = StudentApi.student
    fun requirements(): List<Requirement> = RequirementSource.requirements
    fun completedCredits(): Int = StudentApi.completedCredits
    fun targetTotalCredits(): Int = StudentApi.targetTotalCredits
}