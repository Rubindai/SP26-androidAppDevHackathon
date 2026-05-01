package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.dto.AddCompletedCourseRequest
import com.example.myapplication.data.remote.dto.CreateUserRequest
import com.example.myapplication.data.remote.dto.ProgressResponse
import com.example.myapplication.data.remote.dto.SetDistributionsRequest
import com.example.myapplication.data.remote.dto.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {
    // 201 -> UserResponse with `id`
    // 409 -> error body with `user_id` (netid already taken; treat as login)
    @POST("users/")
    suspend fun createUser(@Body body: CreateUserRequest): Response<UserResponse>

    // Backend has no GET /users/<id>; the list endpoint is the only way to look up a user.
    @GET("users/")
    suspend fun listUsers(): List<UserResponse>

    @GET("users/{userId}/progress/")
    suspend fun getProgress(
        @Path("userId") userId: Int,
        @Query("schedule_id") scheduleId: Int? = null,
    ): ProgressResponse

    // 201 on first add, 200 if already marked complete.
    @POST("users/{userId}/completed-courses/")
    suspend fun addCompletedCourse(
        @Path("userId") userId: Int,
        @Body body: AddCompletedCourseRequest,
    ): Response<Unit>

    // Sets the booleans for the 10 distribution categories (ALC, BIO, ETM, ...).
    @POST("users/{userId}/distributions/")
    suspend fun setDistributions(
        @Path("userId") userId: Int,
        @Body body: SetDistributionsRequest,
    ): Response<Unit>
}
