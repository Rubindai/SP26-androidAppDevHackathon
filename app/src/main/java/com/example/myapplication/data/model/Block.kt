package com.example.myapplication.data.model

data class Block(
    val id: String,
    val day: Day,
    val startHour: Float,
    val endHour: Float,
    val label: String,
    val category: BlockCategory = BlockCategory.OTHER,
)

enum class BlockCategory { CLUB, WORK, SPORT, STUDY, OTHER }
