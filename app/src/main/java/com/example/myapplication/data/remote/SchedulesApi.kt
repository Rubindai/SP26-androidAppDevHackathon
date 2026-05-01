package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.dto.AddOfferingRequest
import com.example.myapplication.data.remote.dto.CreateScheduleRequest
import com.example.myapplication.data.remote.dto.RemoveOfferingRequest
import com.example.myapplication.data.remote.dto.ScheduleDetail
import com.example.myapplication.data.remote.dto.ScheduleListResponse
import com.example.myapplication.data.remote.dto.ScheduleSuggestionsResponse
import com.example.myapplication.data.remote.dto.ScheduleSummary
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SchedulesApi {
    @GET("users/{userId}/schedules/")
    suspend fun listUserSchedules(
        @Path("userId") userId: Int,
        @Query("semester") semester: String? = null,
    ): ScheduleListResponse

    @POST("users/{userId}/schedules/")
    suspend fun createSchedule(
        @Path("userId") userId: Int,
        @Body body: CreateScheduleRequest,
    ): ScheduleSummary

    @GET("schedules/{scheduleId}/")
    suspend fun getSchedule(
        @Path("scheduleId") scheduleId: Int,
    ): ScheduleDetail

    @GET("schedules/{scheduleId}/suggestions/")
    suspend fun getSuggestions(
        @Path("scheduleId") scheduleId: Int,
        @Query("limit") limit: Int = 25,
    ): ScheduleSuggestionsResponse

    // Returns 201 with planned_offerings, 409 on time conflict, 200 if already added.
    // Use Response<T> so callers can read the body of either path.
    @POST("schedules/{scheduleId}/offerings/")
    suspend fun addOffering(
        @Path("scheduleId") scheduleId: Int,
        @Body body: AddOfferingRequest,
    ): Response<Unit>

    // Retrofit doesn't include @DELETE-with-body by default; use @HTTP(hasBody=true).
    @HTTP(method = "DELETE", path = "schedules/{scheduleId}/offerings/", hasBody = true)
    suspend fun removeOffering(
        @Path("scheduleId") scheduleId: Int,
        @Body body: RemoveOfferingRequest,
    ): Response<Unit>
}
