package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Courses
import com.example.myapplication.data.remote.CoursesApi
import com.example.myapplication.data.remote.dto.SemestersResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CourseRepositorySearchTest {
    @Test
    fun searchQueriesCatalogWithoutReplacingCachedCourses() = runBlocking {
        val cachedCourses = listOf(
            Courses(courseId = "CS1110", name = "Intro to Computing", department = "CS", courseNumber = "1110")
        )
        val searchResults = listOf(
            Courses(courseId = "MATH1920", name = "Multivariable Calculus", department = "MATH", courseNumber = "1920")
        )
        val api = RecordingCoursesApi(cachedCourses = cachedCourses, searchResults = searchResults)
        val repository = CourseRepository(api)

        repository.refresh(semester = "FA26").getOrThrow()
        val result = repository.search(semester = "FA26", query = "MATH").getOrThrow()

        assertEquals("FA26", api.lastSemester)
        assertEquals("MATH", api.lastSearch)
        assertEquals(searchResults, result)
        assertEquals(cachedCourses, repository.courses.value)
    }

    private class RecordingCoursesApi(
        private val cachedCourses: List<Courses>,
        private val searchResults: List<Courses>,
    ) : CoursesApi {
        var lastSemester: String? = null
        var lastSearch: String? = null

        override suspend fun listCourses(
            semester: String?,
            subject: String?,
            credits: Int?,
            search: String?,
        ): List<Courses> {
            lastSemester = semester
            lastSearch = search
            return if (search == null) cachedCourses else searchResults
        }

        override suspend fun listSemesters(): SemestersResponse = SemestersResponse(emptyList())
    }
}
