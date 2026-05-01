package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Courses
import com.example.myapplication.data.remote.CoursesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepository @Inject constructor(
    private val api: CoursesApi,
) {
    private val _courses = MutableStateFlow<List<Courses>>(emptyList())
    val courses: StateFlow<List<Courses>> = _courses.asStateFlow()

    suspend fun refresh(semester: String? = null): Result<List<Courses>> = runCatching {
        api.listCourses(semester = semester).also { _courses.value = it }
    }

    suspend fun search(semester: String? = null, query: String): Result<List<Courses>> = runCatching {
        api.listCourses(semester = semester, search = query)
    }

    suspend fun listSemesters(): Result<List<String>> = runCatching {
        api.listSemesters().semesters
    }

    fun byId(courseId: String): Courses? =
        _courses.value.firstOrNull { it.courseId == courseId }

    fun byIds(courseIds: Collection<String>): List<Courses> {
        val all = _courses.value.associateBy { it.courseId }
        return courseIds.mapNotNull { all[it] }
    }
}
