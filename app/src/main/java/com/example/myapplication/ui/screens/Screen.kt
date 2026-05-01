package com.example.myapplication.ui.screens

import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object OnboardingScreen : Screen()

    @Serializable
    data object MajorSelectionScreen : Screen()

    @Serializable
    data object CompletedCoursesScreen : Screen()

    @Serializable
    data object DashboardScreen : Screen()

    @Serializable
    data object SearchScreen : Screen()

    @Serializable
    data object ScheduleScreen : Screen()

    fun NavBackStackEntry.toScreen(): Screen? =
        when (destination.route?.substringAfterLast(".")?.substringBefore("/")) {
            "DashboardScreen" -> toRoute<DashboardScreen>()
            "SearchScreen" -> toRoute<SearchScreen>()
            "ScheduleScreen" -> toRoute<ScheduleScreen>()
            else -> null
        }
}
