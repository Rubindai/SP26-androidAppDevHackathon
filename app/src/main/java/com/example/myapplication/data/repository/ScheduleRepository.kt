package com.example.myapplication.data.repository

import com.example.myapplication.data.local.UserSession
import com.example.myapplication.data.model.Block
import com.example.myapplication.data.model.BlockCategory
import com.example.myapplication.data.model.Courses
import com.example.myapplication.data.model.Day
import com.example.myapplication.data.remote.SchedulesApi
import com.example.myapplication.data.remote.dto.AddOfferingRequest
import com.example.myapplication.data.remote.dto.CreateScheduleRequest
import com.example.myapplication.data.remote.dto.RemoveOfferingRequest
import com.example.myapplication.data.remote.dto.toCourse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepository @Inject constructor(
    private val api: SchedulesApi,
    private val session: UserSession,
) {
    // courseIds (e.g. "INFO2300") that have at least one offering planned in the current schedule.
    // Populated from the backend by loadFromBackend().
    private val _addedIds = MutableStateFlow<Set<String>>(emptySet())
    val addedIds = _addedIds.asStateFlow()

    // Blocks (work-study, clubs) stay local — backend has no concept of these.
    private val _blocks = MutableStateFlow(initialBlocks)
    val blocks = _blocks.asStateFlow()

    // Ensures a Schedule row exists for this user/semester. Saves its id in UserSession.
    // Returns the schedule id on success.
    suspend fun bootstrap(userId: Int, semester: String = "FA26"): Result<Int> = runCatching {
        val list = api.listUserSchedules(userId, semester = semester)
        val existing = list.schedules.firstOrNull { it.semester.equals(semester, ignoreCase = true) }
        val scheduleId = existing?.id
            ?: api.createSchedule(userId, CreateScheduleRequest(semester = semester)).id
        session.setScheduleId(scheduleId)
        scheduleId
    }

    // Loads the planned offerings into _addedIds. Call this after bootstrap or on screen open.
    suspend fun loadFromBackend(): Result<Unit> = runCatching {
        val sid = session.scheduleId.value
            ?: throw IllegalStateException("No schedule selected; sign in first")
        val detail = api.getSchedule(sid)
        _addedIds.value = detail.planned_offerings.map { it.course_code }.toSet()
    }

    suspend fun suggestions(limit: Int = 25): Result<List<Courses>> = runCatching {
        val sid = session.scheduleId.value
            ?: session.userId.value?.let { userId -> bootstrap(userId).getOrThrow() }
            ?: throw IllegalStateException("No schedule selected; sign in first")
        api.getSuggestions(sid, limit = limit).suggestions.map { it.toCourse() }
    }

    // Adds a course to the backend schedule by posting one of its offerings (preferring LEC,
    // since the backend auto-attaches a discussion section when a LEC is added).
    suspend fun add(course: Courses): Result<Unit> = runCatching {
        val sid = requireScheduleId()
        val offering = course.offerings.firstOrNull { it.component?.uppercase() == "LEC" }
            ?: course.offerings.firstOrNull()
            ?: throw IllegalStateException("No offerings available to add")
        val request = AddOfferingRequest(
            class_nbr = offering.classNbr,
            offering_id = offering.id,
        )
        if (request.class_nbr == null && request.offering_id == null) {
            throw IllegalStateException("Offering has no class number or id")
        }

        val response = api.addOffering(sid, request)
        if (!response.isSuccessful) {
            val msg = response.errorBody()?.string()?.take(200) ?: "HTTP ${response.code()}"
            throw IllegalStateException(msg)
        }
        _addedIds.update { it + course.courseId }
    }

    // Removes the course (and any auto-attached sections) from the schedule.
    suspend fun remove(courseId: String): Result<Unit> = runCatching {
        val sid = requireScheduleId()
        val response = api.removeOffering(sid, RemoveOfferingRequest(course_id = courseId))
        if (!response.isSuccessful) {
            val msg = response.errorBody()?.string()?.take(200) ?: "HTTP ${response.code()}"
            throw IllegalStateException(msg)
        }
        _addedIds.update { it - courseId }
    }

    // Convenience for callers who just want to flip — used by the Search "+" button.
    suspend fun toggleAddedRemote(course: Courses): Result<Unit> =
        if (course.courseId in _addedIds.value) remove(course.courseId)
        else add(course)

    fun upsertBlock(block: Block) {
        _blocks.update { current ->
            val index = current.indexOfFirst { it.id == block.id }
            if (index == -1) current + block else current.toMutableList().apply { this[index] = block }
        }
    }

    fun deleteBlock(id: String) {
        _blocks.update { blocks -> blocks.filterNot { it.id == id } }
    }

    private fun requireScheduleId(): Int =
        session.scheduleId.value
            ?: throw IllegalStateException("No schedule selected; sign in first")

    private companion object {
        val initialBlocks = listOf(
            Block("seed-0", Day.MON, 16f, 18f, "Club: Cornell Outing", BlockCategory.CLUB),
            Block("seed-1", Day.WED, 16f, 18f, "Club: Cornell Outing", BlockCategory.CLUB),
            Block("seed-2", Day.TUE, 15f, 17f, "Work study", BlockCategory.WORK),
            Block("seed-3", Day.THU, 15f, 17f, "Work study", BlockCategory.WORK),
        )
    }
}
