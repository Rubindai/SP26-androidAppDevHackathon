package com.example.myapplication.data.model


data class Requirement(
    val title: String,
    val items: List<RequirementItem>,
)

data class RequirementItem(
    val label: String,
    val status: ProgressStatus,
    val course: String,
)

enum class ProgressStatus { COMPLETE, IN_PROGRESS, MISSING, RECOMMENDED }


