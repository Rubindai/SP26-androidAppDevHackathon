package com.example.myapplication.data.repository

import com.example.myapplication.data.remote.CourseApi
import com.example.myapplication.ui.components.courseInformation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepository @Inject constructor(
) {
    fun getAllCourses(): List<courseInformation> {
        return CourseApi.mockCourses
    }
}