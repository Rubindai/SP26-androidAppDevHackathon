package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Courses
import com.example.myapplication.data.model.id
import com.example.myapplication.data.remote.CourseApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepository @Inject constructor(
) {
    fun getAllCourses(): List<Courses> = CourseApi.mockCourses

    fun byId(id: String): Courses? =
        getAllCourses().firstOrNull { it.id == id }

    fun byIds(ids: Collection<String>): List<Courses> {
        val coursesById = getAllCourses().associateBy { it.id }
        return ids.mapNotNull { coursesById[it] }
    }
}
