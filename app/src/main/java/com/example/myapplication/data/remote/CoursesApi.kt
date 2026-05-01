package com.example.myapplication.data.remote

import com.example.myapplication.data.model.Courses
import com.example.myapplication.data.remote.dto.SemestersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CoursesApi {
    // GET /courses?semester=FA26&subject=CS&credits=4&q=algorithms
    // Returns a JSON array of Courses (with their offerings embedded).
    @GET("courses")
    suspend fun listCourses(
        @Query("semester") semester: String? = null,
        @Query("subject") subject: String? = null,
        @Query("credits") credits: Int? = null,
        @Query("q") search: String? = null,
    ): List<Courses>

    @GET("courses/semesters")
    suspend fun listSemesters(): SemestersResponse
}
