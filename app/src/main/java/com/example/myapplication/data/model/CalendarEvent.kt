package com.example.myapplication.data.model

data class CalendarEvent(
    val id: String,
    val day: Day,
    val startHour: Float,
    val endHour: Float,
    val label: String,
    val sublabel: String? = null,
    val color: Long,
    val type: EventType,
)

enum class Day(val code: Char, val short: String, val full: String) {
    MON('M', "Mon", "Monday"),
    TUE('T', "Tue", "Tuesday"),
    WED('W', "Wed", "Wednesday"),
    THU('R', "Thu", "Thursday"),
    FRI('F', "Fri", "Friday"),
    SAT('S', "Sat", "Saturday"),
    SUN('U', "Sun", "Sunday");

    companion object {
        val Week = listOf(MON, TUE, WED, THU, FRI, SAT, SUN)

        fun fromCode(code: Char): Day? =
            entries.firstOrNull { it.code == code.uppercaseChar() }
    }
}

enum class EventType { COURSE, BLOCK }
