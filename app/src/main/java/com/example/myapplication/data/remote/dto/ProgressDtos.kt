package com.example.myapplication.data.remote.dto

import kotlinx.serialization.Serializable

// GET /users/<id>/progress/?schedule_id=<sid>
@Serializable
data class ProgressResponse(
    val groups: List<ProgressGroup> = emptyList(),
    val rules: List<ProgressRule> = emptyList(),
    val remaining_rule_ids: List<Int> = emptyList(),
)

@Serializable
data class ProgressGroup(
    val group_id: String,
    val rules: List<ProgressRule> = emptyList(),
    val scope: String? = null,
    val scope_key: String? = null,
)

@Serializable
data class ProgressRule(
    val rule_id: Int,
    val group_id: String,
    val title: String,
    val status: String,                                     // "complete" | "in_progress" | "remaining" | ...
    val rule_type: String? = null,                          // "choose_n" | "required" | "credits_min"
    val n_required: Int? = null,
    val credits_min: Int? = null,
    val matched_completed: List<String> = emptyList(),
    val matched_planned: List<String> = emptyList(),
    val scope: String? = null,
    val scope_key: String? = null,
)
