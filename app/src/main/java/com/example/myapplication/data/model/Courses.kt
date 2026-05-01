package com.example.myapplication.data.model

import kotlinx.serialization.Serializable

// Mirrors the JSON returned by GET /courses on the backend.
// One Courses object per course (e.g. AAP1100), with all sections inside `offerings`.
@Serializable
data class Courses(
    val courseId: String = "",          // "INFO2300" — server's stable identifier
    val name: String = "",
    val department: String = "",
    val courseNumber: String = "",
    val credits: Int = 0,
    val description: String = "",
    val prerequisites: String = "",
    val corequisites: String = "",
    val distributions: String = "",     // e.g. "(ALC-AS), (SBA-AS)"
    val tags: List<String> = emptyList(),
    val offerings: List<Offering> = emptyList(),
    val open: Boolean = false,
)

@Serializable
data class Offering(
    val id: Int? = null,
    val semester: String = "",          // "FA26"
    val classNbr: Int? = null,
    val section: String? = null,
    val component: String? = null,      // "LEC", "DIS"
    val instructor: String? = null,
    val days: String? = null,           // "MWF", "TR"
    val startTime: String? = null,
    val endTime: String? = null,
    val time: String? = null,           // "02:30PM-04:10PM"
    val location: String? = null,
)

// --- View-friendly accessors ---
// The UI was written assuming "one card = one course with one instructor/time/etc.",
// while the backend models a course as having many offerings. These extensions pick
// the first offering so existing screens keep working without per-section UI changes.

val Courses.code: String get() = "$department $courseNumber"
val Courses.primaryOffering: Offering? get() = offerings.firstOrNull()
val Courses.instructor: String get() = primaryOffering?.instructor.orEmpty()
val Courses.days: String get() = primaryOffering?.days.orEmpty()
val Courses.time: String get() = primaryOffering?.time.orEmpty()
val Courses.location: String get() = primaryOffering?.location.orEmpty()

// "FA26" -> "Fall", "SP26" -> "Spring"
val Courses.semester: String get() = when (primaryOffering?.semester?.take(2)) {
    "FA" -> "Fall"
    "SP" -> "Spring"
    "SU" -> "Summer"
    "WI" -> "Winter"
    else -> ""
}

// "FA26" -> 2026
val Courses.year: Int
    get() = primaryOffering?.semester?.drop(2)?.toIntOrNull()?.let { 2000 + it } ?: 0
