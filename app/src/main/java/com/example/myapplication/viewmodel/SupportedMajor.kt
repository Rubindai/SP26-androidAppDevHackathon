package com.example.myapplication.viewmodel

data class SupportedMajor(
    val code: String,
    val label: String,
    val description: String,
) {
    companion object {
        val options = listOf(
            SupportedMajor(
                code = "CS",
                label = "Computer Science",
                description = "Software, systems, theory, AI, and computing foundations",
            ),
            SupportedMajor(
                code = "ECE",
                label = "Electrical and Computer Engineering",
                description = "Circuits, signals, hardware, embedded systems, and computing",
            ),
            SupportedMajor(
                code = "ORIE",
                label = "Operations Research",
                description = "Optimization, probability, analytics, and decision systems",
            ),
        )
    }
}

fun normalizeSupportedMajor(input: String): String? {
    val normalized = input.trim().uppercase()
    return when (normalized) {
        "CS", "COMPUTER SCIENCE" -> "CS"
        "ECE", "ELECTRICAL AND COMPUTER ENGINEERING" -> "ECE"
        "ORIE",
        "OPERATIONS RESEARCH",
        "OPERATIONS RESEARCH AND INFORMATION ENGINEERING" -> "ORIE"
        else -> null
    }
}
