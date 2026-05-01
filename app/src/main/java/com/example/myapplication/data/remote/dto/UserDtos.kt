package com.example.myapplication.data.remote.dto

import kotlinx.serialization.Serializable

// POST /users/ request body. Backend accepts both camelCase and snake_case;
// we send camelCase to match Kotlin's defaults.
@Serializable
data class CreateUserRequest(
    val name: String,
    val netid: String,
    val major: String,
    val school: String,
    val college: String,
    val catalog_year: String,
    val year: Int,
    val targetTerm: String,
    val targetCreditsLow: Int,
    val targetCreditsHigh: Int,
)

// Single DTO that handles both 201 (success) and 409 (duplicate netid) bodies.
// On 201 the server returns "id"; on 409 it returns "user_id" plus an "error".
// We extract the user id with the `effectiveId` helper.
@Serializable
data class UserResponse(
    val id: Int? = null,
    val user_id: Int? = null,
    val error: String? = null,
    val netid: String? = null,
    val name: String? = null,
    val initial: String? = null,
    val major: String? = null,
    val college: String? = null,
    val year: Int? = null,
    val target_term: String? = null,
    val target_credits_low: Int? = null,
    val target_credits_high: Int? = null,
    val completed: List<String> = emptyList(),
    val completed_distributions: List<String> = emptyList(),
)

val UserResponse.effectiveId: Int? get() = id ?: user_id

// POST /users/<id>/completed-courses/  body
@Serializable
data class AddCompletedCourseRequest(
    val course_id: String,           // course code like "MATH1110"
)

// POST /users/<id>/distributions/  body — 10 boolean flags
@Serializable
data class SetDistributionsRequest(
    val ALC: Boolean = false,
    val BIO: Boolean = false,
    val ETM: Boolean = false,
    val GLC: Boolean = false,
    val HST: Boolean = false,
    val PHS: Boolean = false,
    val SCD: Boolean = false,
    val SSC: Boolean = false,
    val SDS: Boolean = false,
    val SMR: Boolean = false,
)
