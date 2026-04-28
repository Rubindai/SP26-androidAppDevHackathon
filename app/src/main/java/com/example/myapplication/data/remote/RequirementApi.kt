package com.example.myapplication.data.remote

import com.example.myapplication.data.model.ProgressStatus
import com.example.myapplication.data.model.Requirement
import com.example.myapplication.data.model.RequirementItem


object RequirementSource {
    val requirements: List<Requirement> = listOf(
        Requirement(
            title = "First-Year Foundations",
            items = listOf(
                RequirementItem("First-Year Writing I", ProgressStatus.COMPLETE, "ENGL 1170"),
                RequirementItem("First-Year Writing II", ProgressStatus.MISSING, "ENGL 1270"),
                RequirementItem("Calculus I", ProgressStatus.COMPLETE, "MATH 1110"),
                RequirementItem("Intro to Computing", ProgressStatus.IN_PROGRESS, "CS 1110"),
            ),
        ),
        Requirement(
            title = "CS Core",
            items = listOf(
                RequirementItem(
                    "Object-Oriented Data Structures",
                    ProgressStatus.COMPLETE,
                    "CS 2110"
                ),
                RequirementItem("Discrete Structures", ProgressStatus.IN_PROGRESS, "CS 2800"),
                RequirementItem("Functional Programming", ProgressStatus.RECOMMENDED, "CS 3110"),
                RequirementItem("Computer System Organization", ProgressStatus.MISSING, "CS 3410"),
                RequirementItem("Algorithms", ProgressStatus.MISSING, "CS 4820"),
            ),
        ),
        Requirement(
            title = "Math Co-requirements",
            items = listOf(
                RequirementItem("Calculus II", ProgressStatus.COMPLETE, "MATH 1120"),
                RequirementItem("Linear Algebra", ProgressStatus.RECOMMENDED, "MATH 2940"),
                RequirementItem("Probability", ProgressStatus.MISSING, "CS 4850"),
            ),
        ),
        Requirement(
            title = "CS Electives (4000+)",
            items = listOf(
                RequirementItem("CS Elective", ProgressStatus.COMPLETE, "CS 4780"),
                RequirementItem("CS Elective", ProgressStatus.RECOMMENDED, "CS 4410"),
                RequirementItem("CS Elective", ProgressStatus.MISSING, "CS 4670"),
                RequirementItem("CS Project Course", ProgressStatus.MISSING, "CS 5150"),
            ),
        ),
        Requirement(
            title = "Breadth Distributions",
            items = listOf(
                RequirementItem("Historical Analysis (HA)", ProgressStatus.MISSING, "HIST 1530"),
                RequirementItem(
                    "Knowledge, Cog & Moral Reasoning (KCM)",
                    ProgressStatus.COMPLETE,
                    "PHIL 1440"
                ),
                RequirementItem("Social & Behavioral (SBA)", ProgressStatus.COMPLETE, "PSYCH 1101"),
                RequirementItem(
                    "Literature & the Arts (LA)",
                    ProgressStatus.RECOMMENDED,
                    "ENGL 2800"
                ),
                RequirementItem("Cultural Analysis (CA)", ProgressStatus.MISSING, "ANTHR 1101"),
            ),
        ),
        Requirement(
            title = "Foreign Language",
            items = listOf(
                RequirementItem("Language Sequence I", ProgressStatus.IN_PROGRESS, "SPAN 2000"),
                RequirementItem("Language Sequence II", ProgressStatus.MISSING, "SPAN 2090"),
            ),
        ),
        Requirement(
            title = "Total Credits",
            items = listOf(
                RequirementItem("120 credits to graduate", ProgressStatus.IN_PROGRESS, "47 / 120"),
            ),
        ),
    )
}
