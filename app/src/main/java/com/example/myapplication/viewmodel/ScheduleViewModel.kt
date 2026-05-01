package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Block
import com.example.myapplication.data.model.CalendarEvent
import com.example.myapplication.data.model.Courses
import com.example.myapplication.data.model.Day
import com.example.myapplication.data.model.EventType
import com.example.myapplication.data.model.code
import com.example.myapplication.data.model.days
import com.example.myapplication.data.model.location
import com.example.myapplication.data.model.time
import com.example.myapplication.data.repository.CourseRepository
import com.example.myapplication.data.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val scheduleRepository: ScheduleRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Load planned offerings + the course catalog needed to render them.
            // Both calls are best-effort; if either fails the UI just shows what it has.
            scheduleRepository.loadFromBackend()
            courseRepository.refresh(semester = "FA26")
        }
        viewModelScope.launch {
            // Re-derive whenever any of the three flows change (added, blocks, or course catalog).
            combine(
                scheduleRepository.addedIds,
                scheduleRepository.blocks,
                courseRepository.courses,
            ) { addedIds, blocks, _ ->
                buildUiState(addedIds, blocks)
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    data class ScheduleUiState(
        val events: List<CalendarEvent> = emptyList(),
        val conflictIds: Set<String> = emptySet(),
        val conflictPairCount: Int = 0,
        val totalCredits: Int = 0,
        val courseCount: Int = 0,
        val addedCourses: List<Courses> = emptyList(),
        val blocks: List<Block> = emptyList(),
        val term: String = "Spring 2026",
    ) {
        val hasConflict: Boolean get() = conflictPairCount > 0
        val isOverCreditLoad: Boolean get() = totalCredits > 17
    }

    fun removeCourse(courseId: String) {
        viewModelScope.launch {
            scheduleRepository.remove(courseId)
        }
    }

    fun upsertBlock(block: Block) {
        scheduleRepository.upsertBlock(block)
    }

    fun deleteBlock(id: String) {
        scheduleRepository.deleteBlock(id)
    }

    private fun buildUiState(
        addedIds: Set<String>,
        blocks: List<Block>,
    ): ScheduleUiState {
        val courses = courseRepository.byIds(addedIds)
        val events = scheduleCalendarEvents(courses, blocks)
        val conflicts = findConflicts(events)

        return ScheduleUiState(
            events = events,
            conflictIds = conflicts.flatMap { listOf(it.first, it.second) }.toSet(),
            conflictPairCount = conflicts.size,
            totalCredits = courses.sumOf { it.credits },
            courseCount = courses.size,
            addedCourses = courses,
            blocks = blocks,
        )
    }
}

fun scheduleCalendarEvents(
    courses: List<Courses>,
    blocks: List<Block> = emptyList(),
): List<CalendarEvent> =
    courses.flatMap { course -> course.toCalendarEvents() } + blocks.map { block -> block.toCalendarEvent() }

private fun Courses.toCalendarEvents(): List<CalendarEvent> {
    val timeRange = parseTimeRange(time) ?: return emptyList()
    return parseDays(days).map { day ->
        CalendarEvent(
            id = "course:$courseId:${day.code}",
            day = day,
            startHour = timeRange.first,
            endHour = timeRange.second,
            label = code,
            sublabel = "$time · $location",
            color = calendarColor(),
            type = EventType.COURSE,
        )
    }
}

private fun Courses.calendarColor(): Long =
    if (open) 0xFFB31B1B else 0xFF775653

private fun Block.toCalendarEvent(): CalendarEvent =
    CalendarEvent(
        id = "block:$id",
        day = day,
        startHour = startHour,
        endHour = endHour,
        label = label,
        color = 0xFF775653,
        type = EventType.BLOCK,
    )

private fun findConflicts(events: List<CalendarEvent>): List<Pair<String, String>> {
    val conflicts = mutableListOf<Pair<String, String>>()

    for (i in events.indices) {
        for (j in i + 1 until events.size) {
            if (events[i].overlaps(events[j])) {
                conflicts += events[i].id to events[j].id
            }
        }
    }

    return conflicts
}

private fun CalendarEvent.overlaps(other: CalendarEvent): Boolean =
    day == other.day && startHour < other.endHour && other.startHour < endHour

private fun parseDays(value: String): List<Day> {
    val text = value.uppercase().replace(" ", "")
    val days = mutableListOf<Day>()
    var index = 0

    while (index < text.length) {
        when {
            text.startsWith("SU", index) -> {
                days += Day.SUN
                index += 2
            }

            text.startsWith("TH", index) -> {
                days += Day.THU
                index += 2
            }

            else -> {
                Day.fromCode(text[index])?.let { days += it }
                index += 1
            }
        }
    }

    return days
}

private fun parseTimeRange(value: String): Pair<Float, Float>? {
    val parts = value.replace("–", "-").split("-").map { it.trim() }
    if (parts.size != 2) return null

    val start = parseClock(parts[0]) ?: return null
    val end = parseClock(parts[1]) ?: return null
    return start to end
}

private fun parseClock(value: String): Float? {
    val match = clockRegex.find(value.trim()) ?: return null
    val hour = match.groupValues[1].toIntOrNull() ?: return null
    val minute = match.groupValues[2].takeIf { it.isNotBlank() }?.toIntOrNull() ?: 0
    val period = match.groupValues[3].uppercase()

    val hour24 = when {
        period == "PM" && hour != 12 -> hour + 12
        period == "AM" && hour == 12 -> 0
        else -> hour
    }

    return hour24 + minute / 60f
}

private val clockRegex = Regex("""(\d{1,2})(?::(\d{2}))?\s*(AM|PM)""", RegexOption.IGNORE_CASE)
