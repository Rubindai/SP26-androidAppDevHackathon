package com.example.myapplication.data.repository

import com.example.myapplication.data.local.UserSession
import com.example.myapplication.data.model.ProgressStatus
import com.example.myapplication.data.model.Requirement
import com.example.myapplication.data.model.RequirementItem
import com.example.myapplication.data.model.Student
import com.example.myapplication.data.remote.RequirementSource
import com.example.myapplication.data.remote.StudentApi
import com.example.myapplication.data.remote.UsersApi
import com.example.myapplication.data.remote.dto.AddCompletedCourseRequest
import com.example.myapplication.data.remote.dto.CreateUserRequest
import com.example.myapplication.data.remote.dto.ProgressResponse
import com.example.myapplication.data.remote.dto.SetDistributionsRequest
import com.example.myapplication.data.remote.dto.UserResponse
import com.example.myapplication.data.remote.dto.effectiveId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepository @Inject constructor(
    private val usersApi: UsersApi,
    private val session: UserSession,
) {
    private val _currentStudent = MutableStateFlow(StudentApi.student)
    val currentStudent: StateFlow<Student> = _currentStudent.asStateFlow()

    fun updateStudent(newStudent: Student) {
        _currentStudent.value = newStudent
    }

    // Pulls the canonical Student from the backend (by saved netid) and updates _currentStudent.
    // Backend has no GET /users/<id>, so we fetch the list and filter.
    suspend fun refreshCurrentUser(): Result<Student> = runCatching {
        val netid = session.netid.value
            ?: throw IllegalStateException("Not signed in")
        val match = usersApi.listUsers().firstOrNull { it.netid == netid }
            ?: throw IllegalStateException("User $netid not found on server")
        val student = match.toStudent()
        _currentStudent.value = student
        student
    }

    suspend fun signIn(student: Student): Result<Int> = runCatching {
        val request = CreateUserRequest(
            name = student.name,
            netid = student.netid,
            major = student.major.ifBlank { "CS" },
            school = "engineering",
            college = student.college.ifBlank { "College of Engineering" },
            catalog_year = "any",
            year = student.year.toIntOrNull() ?: 2027,
            targetTerm = student.targetTerm.ifBlank { "FA26" },
            targetCreditsLow = student.targetCreditsLow.takeIf { it > 0 } ?: 13,
            targetCreditsHigh = student.targetCreditsHigh.takeIf { it >= 13 } ?: 17,
        )

        val response = usersApi.createUser(request)
        val body: UserResponse? = when {
            response.isSuccessful -> response.body()
            response.code() == 409 -> response.errorBody()?.string()?.let { json.decodeFromString<UserResponse>(it) }
            else -> {
                val msg = response.errorBody()?.string() ?: "HTTP ${response.code()}"
                throw IllegalStateException(msg)
            }
        }

        val userId = body?.effectiveId
            ?: throw IllegalStateException("Server response did not include a user id")

        session.setUser(id = userId, netid = student.netid)
        // Prefer the server's canonical view of the user (it has applied defaults like
        // school/catalog_year and reconciled types). Fall back to the form input if the
        // 409 path didn't include the full profile.
        _currentStudent.value = body.toStudent().takeIf { it.name.isNotBlank() } ?: student
        userId
    }

    suspend fun fetchProgress(): Result<ProgressResponse> = runCatching {
        val userId = session.userId.value
            ?: throw IllegalStateException("Not signed in")
        usersApi.getProgress(userId, scheduleId = session.scheduleId.value)
    }

    suspend fun markCompleted(courseId: String): Result<Unit> = runCatching {
        val userId = session.userId.value
            ?: throw IllegalStateException("Not signed in")
        val response = usersApi.addCompletedCourse(userId, AddCompletedCourseRequest(courseId))
        if (!response.isSuccessful) {
            throw IllegalStateException(response.errorBody()?.string()?.take(200) ?: "HTTP ${response.code()}")
        }
    }

    suspend fun saveDistributions(req: SetDistributionsRequest): Result<Unit> = runCatching {
        val userId = session.userId.value
            ?: throw IllegalStateException("Not signed in")
        val response = usersApi.setDistributions(userId, req)
        if (!response.isSuccessful) {
            throw IllegalStateException(response.errorBody()?.string()?.take(200) ?: "HTTP ${response.code()}")
        }
    }

    fun student(): Student = StudentApi.student
    fun requirements(): List<Requirement> = RequirementSource.requirements
    fun completedCredits(): Int = StudentApi.completedCredits
    fun targetTotalCredits(): Int = StudentApi.targetTotalCredits

    private companion object {
        val json = Json { ignoreUnknownKeys = true; isLenient = true; coerceInputValues = true }
    }
}

// Maps the backend's progress payload into the existing UI-side Requirement / RequirementItem
// model so ProgressScreen can render without redesign.
fun ProgressResponse.toRequirements(): List<Requirement> = groups.map { group ->
    Requirement(
        title = humanizeGroupId(group.group_id),
        items = group.rules.map { rule ->
            RequirementItem(
                label = rule.title,
                status = when (rule.status.lowercase()) {
                    "complete", "satisfied" -> ProgressStatus.COMPLETE
                    "in_progress" -> ProgressStatus.IN_PROGRESS
                    "recommended" -> ProgressStatus.RECOMMENDED
                    else -> ProgressStatus.MISSING
                },
                course = rule.matched_completed.firstOrNull()
                    ?: rule.matched_planned.firstOrNull()
                    ?: requirementHint(rule),
            )
        }
    )
}

private fun requirementHint(rule: com.example.myapplication.data.remote.dto.ProgressRule): String = when {
    rule.credits_min != null -> "${rule.credits_min} cr"
    rule.n_required != null -> "Choose ${rule.n_required}"
    else -> ""
}

// Server profile -> UI Student. Used after sign-in and by refreshCurrentUser().
fun UserResponse.toStudent(): Student = Student(
    name = name.orEmpty(),
    initial = initial.orEmpty(),
    netid = netid.orEmpty(),
    major = major.orEmpty(),
    year = year?.toString().orEmpty(),
    targetTerm = target_term.orEmpty(),
    targetCreditsLow = target_credits_low ?: 0,
    targetCreditsHigh = target_credits_high ?: 120,
    completed = completed,
    college = college.orEmpty(),
)

private fun humanizeGroupId(id: String): String =
    id.split("_").joinToString(" ") { word ->
        when (word.uppercase()) {
            "CS", "OOP", "AI", "ML", "OS" -> word.uppercase()
            else -> word.replaceFirstChar { c -> c.uppercase() }
        }
    }
