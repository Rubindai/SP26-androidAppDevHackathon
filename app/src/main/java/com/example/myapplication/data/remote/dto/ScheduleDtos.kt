package com.example.myapplication.data.remote.dto

import com.example.myapplication.data.model.Courses
import com.example.myapplication.data.model.Offering
import kotlinx.serialization.Serializable

// POST /users/<id>/schedules/  body
@Serializable
data class CreateScheduleRequest(
    val semester: String,
    val name: String? = null,
    val status: String? = null,
)

// 201 response from POST /users/<id>/schedules/, also matches the summary objects
// in the GET /users/<id>/schedules/ array (with optional planned_count).
@Serializable
data class ScheduleSummary(
    val id: Int,
    val user_id: Int,
    val name: String,
    val semester: String,
    val status: String,
    val planned_count: Int = 0,
)

// GET /users/<id>/schedules/  envelope
@Serializable
data class ScheduleListResponse(
    val schedules: List<ScheduleSummary> = emptyList(),
)

// GET /schedules/<sid>/  detailed view
@Serializable
data class ScheduleDetail(
    val id: Int,
    val user_id: Int,
    val name: String,
    val semester: String,
    val status: String,
    val planned_offerings: List<PlannedOffering> = emptyList(),
)

// One row inside ScheduleDetail.planned_offerings — has offering_id + course_code
// plus the same offering fields the OfferingDto carries.
@Serializable
data class PlannedOffering(
    val offering_id: Int,
    val course_code: String,
    val semester: String? = null,
    val classNbr: Int? = null,
    val section: String? = null,
    val component: String? = null,
    val instructor: String? = null,
    val days: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val time: String? = null,
    val location: String? = null,
)

// POST /schedules/<sid>/offerings/  body — supply class_nbr OR offering_id
@Serializable
data class AddOfferingRequest(
    val class_nbr: Int? = null,
    val offering_id: Int? = null,
)

// DELETE /schedules/<sid>/offerings/  body — supply course_id OR offering_id
@Serializable
data class RemoveOfferingRequest(
    val course_id: String? = null,
    val offering_id: Int? = null,
)

// GET /schedules/<sid>/suggestions/?limit=25
@Serializable
data class ScheduleSuggestionsResponse(
    val schedule_id: Int,
    val suggestions: List<ScheduleSuggestionDto> = emptyList(),
)

@Serializable
data class ScheduleSuggestionDto(
    val offering_id: Int,
    val course_id: String,
    val course_number: String = "",
    val title: String = "",
    val semester: String = "",
    val section: String? = null,
    val instructor: String? = null,
    val days: String? = null,
    val start_time: String? = null,
    val end_time: String? = null,
    val location: String? = null,
)

fun ScheduleSuggestionDto.toCourse(): Courses {
    val number = course_number.ifBlank { course_id.dropWhile { it.isLetter() } }
    val department = course_id.removeSuffix(number).ifBlank {
        course_id.takeWhile { it.isLetter() }
    }
    val time = when {
        !start_time.isNullOrBlank() && !end_time.isNullOrBlank() -> "$start_time-$end_time"
        !start_time.isNullOrBlank() -> start_time
        else -> null
    }

    return Courses(
        courseId = course_id,
        name = title,
        department = department,
        courseNumber = number,
        offerings = listOf(
            Offering(
                id = offering_id,
                semester = semester,
                section = section,
                component = "LEC",
                instructor = instructor,
                days = days,
                startTime = start_time,
                endTime = end_time,
                time = time,
                location = location,
            )
        ),
        open = true,
    )
}
